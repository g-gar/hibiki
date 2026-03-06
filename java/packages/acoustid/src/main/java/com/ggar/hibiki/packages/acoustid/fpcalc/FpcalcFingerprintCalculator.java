package com.ggar.hibiki.packages.acoustid.fpcalc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggar.hibiki.packages.acoustid.model.AudioFingerprint;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Implementation of {@link FingerprintCalculator} that uses the native `fpcalc`
 * utility.
 * It pipes the {@link InputStream} into `fpcalc` and parses the JSON output.
 */
@RequiredArgsConstructor
@Slf4j
public class FpcalcFingerprintCalculator implements FingerprintCalculator {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<AudioFingerprint> calculate(InputStream audioStream) {
        return Mono.fromCallable(() -> {
                    log.debug("Tracing fpcalc from input stream");
                    ProcessBuilder processBuilder = new ProcessBuilder("fpcalc", "-json", "-");
                    processBuilder.redirectErrorStream(true);
                    Process process = processBuilder.start();

                    Thread piper = new Thread(() -> {
                        try (java.io.OutputStream os = process.getOutputStream()) {
                            byte[] buffer = new byte[8192];
                            int len;
                            while ((len = audioStream.read(buffer)) != -1) {
                                os.write(buffer, 0, len);
                            }
                        } catch (Exception e) {
                            log.error("Error piping into fpcalc stream", e);
                        }
                    });
                    piper.start();

                    try (InputStream is = process.getInputStream()) {
                        JsonNode rootNode = objectMapper.readTree(is);
                        int exitCode = process.waitFor();
                        piper.join(1000);

                        if (exitCode != 0) {
                            throw new RuntimeException(
                                    "fpcalc exited with code " + exitCode + ". Output: " + rootNode.toString());
                        }

                        String fingerprint = rootNode.get("fingerprint").asText();
                        int duration = rootNode.get("duration").asInt();

                        return AudioFingerprint.builder()
                                .fingerprint(fingerprint)
                                .durationSeconds(duration)
                                .build();
                    } finally {
                        if (process.isAlive()) {
                            process.destroy();
                        }
                    }
                })
                .subscribeOn(Schedulers.boundedElastic());
    }
}
