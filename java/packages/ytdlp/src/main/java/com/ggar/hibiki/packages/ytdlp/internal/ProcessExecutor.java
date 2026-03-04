package com.ggar.hibiki.packages.ytdlp.internal;

import com.ggar.hibiki.packages.ytdlp.logging.Logger;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Internal utility to execute OS processes and stream their output reactively.
 */
public class ProcessExecutor {

    private final Logger logger;

    public ProcessExecutor(Logger logger) {
        this.logger = logger;
    }

    /**
     * Executes a command and returns its standard output as a reactive stream of
     * lines.
     * Standard error is consumed and logged internally.
     * 
     * @param command          The execution command and arguments.
     * @param workingDirectory The directory to run the process in, or null for
     *                         current.
     * @return Flux of stdout lines.
     */
    public Flux<String> execute(List<String> command, String workingDirectory) {
        return Flux.create(sink -> {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                if (workingDirectory != null) {
                    processBuilder.directory(new File(workingDirectory));
                }

                logger.debug("Executing process: " + String.join(" ", command));
                Process process = processBuilder.start();

                // Consume stderr in a separate virtual/background thread to prevent blocking
                new Thread(() -> {
                    try (BufferedReader errorReader = new BufferedReader(
                            new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = errorReader.readLine()) != null) {
                            logger.error("yt-dlp error: " + line);
                        }
                    } catch (Exception e) {
                        logger.error("Failed to read process stderr", e);
                    }
                }, "ytdlp-stderr-consumer-" + process.pid()).start();

                // Consume stdout and push to Flux
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (sink.isCancelled()) {
                            process.destroy();
                            break;
                        }
                        sink.next(line);
                    }
                }

                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    sink.error(new RuntimeException("Process exited with error code: " + exitCode));
                } else {
                    sink.complete();
                }

            } catch (Exception e) {
                sink.error(new RuntimeException("Failed to execute process", e));
            }
        });
    }
}
