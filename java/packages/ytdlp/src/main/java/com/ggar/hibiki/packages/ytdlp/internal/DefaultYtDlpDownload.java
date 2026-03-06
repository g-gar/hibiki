package com.ggar.hibiki.packages.ytdlp.internal;

import com.ggar.hibiki.packages.ytdlp.builder.YtDlpDownload;
import com.ggar.hibiki.packages.ytdlp.config.YtDlpProperties;
import com.ggar.hibiki.packages.ytdlp.model.DownloadEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class DefaultYtDlpDownload implements YtDlpDownload {

    private final String url;
    private final ProcessExecutor processExecutor;
    private final YtDlpProperties properties;
    private boolean extractAudio = false;
    private String audioFormat = null;
    private Integer audioQuality = null;
    private boolean writeInfoJson = false;
    private String outputTemplate = null;

    private static final Pattern PROGRESS_PATTERN = Pattern.compile("\\[download\\]\\s+([\\d.]+)%");
    private static final Pattern DESTINATION_PATTERN = Pattern.compile("\\[download\\] Destination: (.*)");
    private static final Pattern COMPLETED_PATTERN = Pattern.compile("\\[download\\] 100% of .* in .*");

    protected DefaultYtDlpDownload(String url, ProcessExecutor processExecutor, YtDlpProperties properties) {
        this.url = url;
        this.processExecutor = processExecutor;
        this.properties = properties;
    }

    @Override
    public YtDlpDownload extractAudio() {
        this.extractAudio = true;
        return this;
    }

    @Override
    public YtDlpDownload audioFormat(String format) {
        this.audioFormat = format;
        return this;
    }

    @Override
    public YtDlpDownload audioQuality(int quality) {
        this.audioQuality = quality;
        return this;
    }

    @Override
    public YtDlpDownload writeInfoJson() {
        this.writeInfoJson = true;
        return this;
    }

    @Override
    public YtDlpDownload outputTemplate(String template) {
        this.outputTemplate = template;
        return this;
    }

    @Override
    public Flux<DownloadEvent> executeTo(String targetDirectory) {
        List<String> args = buildArgs();

        return processExecutor
                .execute(args, targetDirectory)
                .flatMap(line -> {
                    try {
                        return Mono.justOrEmpty(parseEvent(line));
                    } catch (Exception e) {
                        log.error("Failed to parse yt-dlp download event line: " + line, e);
                        return Mono.empty();
                    }
                })
                .doOnSubscribe(subscription -> log.info("Starting yt-dlp download: " + url))
                .doOnComplete(() -> log.info("Download completed successfully: " + url));
    }

    private List<String> buildArgs() {
        List<String> args = new ArrayList<>();
        args.add(properties.getBinaryPath());

        args.add("--newline"); // Crucial for reliable stdout parsing block by block

        if (extractAudio) {
            args.add("--extract-audio");
            if (audioFormat != null) {
                args.add("--audio-format");
                args.add(audioFormat);
            }
            if (audioQuality != null) {
                args.add("--audio-quality");
                args.add(String.valueOf(audioQuality));
            }
        }

        if (writeInfoJson) {
            args.add("--write-info-json");
        }

        if (outputTemplate != null) {
            args.add("-o");
            args.add(outputTemplate);
        } else if (properties.getDefaultOutputTemplate() != null) {
            args.add("-o");
            args.add(properties.getDefaultOutputTemplate());
        }

        args.add(url);
        return args;
    }

    private DownloadEvent parseEvent(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        Matcher progressMatcher = PROGRESS_PATTERN.matcher(line);
        if (progressMatcher.find()) {
            double percentage = Double.parseDouble(progressMatcher.group(1));
            return DownloadEvent.builder()
                    .type(DownloadEvent.EventType.PROGRESS)
                    .progressPercentage(percentage)
                    .message(line)
                    .build();
        }

        Matcher destMatcher = DESTINATION_PATTERN.matcher(line);
        if (destMatcher.find()) {
            return DownloadEvent.builder()
                    .type(DownloadEvent.EventType.STARTING)
                    .savedFilePath(destMatcher.group(1))
                    .message(line)
                    .build();
        }

        if (COMPLETED_PATTERN.matcher(line).find() || line.contains("has already been downloaded")) {
            return DownloadEvent.builder()
                    .type(DownloadEvent.EventType.FINISHED)
                    .message(line)
                    .build();
        }

        if (line.contains("ERROR:") || line.toLowerCase().contains("failed to")) {
            return DownloadEvent.builder()
                    .type(DownloadEvent.EventType.ERROR)
                    .message(line)
                    .build();
        }

        return null;
    }
}
