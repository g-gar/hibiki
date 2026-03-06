package com.ggar.hibiki.packages.ytdlp.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggar.hibiki.packages.ytdlp.builder.YtDlpRequest;
import com.ggar.hibiki.packages.ytdlp.config.YtDlpProperties;
import com.ggar.hibiki.packages.ytdlp.model.Chapter;
import com.ggar.hibiki.packages.ytdlp.model.MediaFormat;
import com.ggar.hibiki.packages.ytdlp.model.VideoMetadata;
import com.ggar.hibiki.packages.ytdlp.model.VideoSearchResult;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class DefaultYtDlpRequest implements YtDlpRequest {

    private final String target;
    private final ProcessExecutor processExecutor;
    private final YtDlpProperties properties;
    private final ObjectMapper objectMapper;
    private boolean flatPlaylist = false;
    private Integer maxResults = null;

    protected DefaultYtDlpRequest(
            String target, ProcessExecutor processExecutor, YtDlpProperties properties, ObjectMapper objectMapper) {
        this.target = target;
        this.processExecutor = processExecutor;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    public YtDlpRequest flatPlaylist() {
        this.flatPlaylist = true;
        return this;
    }

    @Override
    public YtDlpRequest maxResults(int limit) {
        this.maxResults = limit;
        return this;
    }

    @Override
    public Flux<VideoSearchResult> execute() {
        List<String> args = buildArgs();

        return processExecutor
                .execute(args, properties.getScratchpadDirectory())
                .flatMap(line -> {
                    if (line == null || line.trim().isEmpty()) {
                        return Mono.empty();
                    }

                    try {
                        VideoSearchResult result = objectMapper.readValue(line, VideoSearchResult.class);
                        return Mono.just(result);
                    } catch (Exception e) {
                        log.error("Failed to parse yt-dlp metadata line: " + line, e);
                        return Mono.empty();
                    }
                });
    }

    @Override
    public Mono<VideoMetadata> executeForDeepMetadata() {
        List<String> args = buildArgs();
        // Since we explicitly want a deeply parsed single video model, force
        // no-playlist regardless of setup.
        if (!args.contains("--no-playlist") && !flatPlaylist) {
            args.add("--no-playlist");
        }

        return processExecutor
                .execute(args, properties.getScratchpadDirectory())
                .next()
                .flatMap(line -> {
                    try {
                        JsonNode node = objectMapper.readTree(line);

                        List<Chapter> chapters = new ArrayList<>();
                        JsonNode chaptersNode = node.path("chapters");
                        if (chaptersNode.isArray()) {
                            for (JsonNode chapNode : chaptersNode) {
                                chapters.add(Chapter.builder()
                                        .title(chapNode.path("title").asText(null))
                                        .startTime(chapNode.path("start_time").asDouble(0.0))
                                        .endTime(chapNode.path("end_time").asDouble(0.0))
                                        .build());
                            }
                        }

                        List<MediaFormat> formats = new ArrayList<>();
                        JsonNode formatsNode = node.path("formats");
                        if (formatsNode.isArray()) {
                            for (JsonNode formatNode : formatsNode) {
                                try {
                                    MediaFormat format = MediaFormat.builder()
                                            .formatId(
                                                    formatNode.path("format_id").asText(null))
                                            .ext(formatNode.path("ext").asText(null))
                                            .resolution(formatNode
                                                    .path("resolution")
                                                    .asText(null))
                                            .vcodec(formatNode.path("vcodec").asText(null))
                                            .acodec(formatNode.path("acodec").asText(null))
                                            .abr(formatNode.path("abr").asDouble(0.0))
                                            .vbr(formatNode.path("vbr").asDouble(0.0))
                                            .tbr(formatNode.path("tbr").asDouble(0.0))
                                            .filesize(
                                                    formatNode.path("filesize").asLong(0L))
                                            .build();
                                    formats.add(format);
                                } catch (Exception e) {
                                    log.error("Failed to parse format metadata: " + formatNode, e);
                                }
                            }
                        }

                        VideoMetadata metadata = VideoMetadata.builder()
                                .id(node.path("id").asText(null))
                                .title(node.path("title").asText(null))
                                .description(node.path("description").asText(null))
                                .uploader(node.path("uploader").asText(null))
                                .duration(node.path("duration").asDouble(0.0))
                                .webpageUrl(node.path("webpage_url").asText(null))
                                .isLive(node.path("is_live").asBoolean(false))
                                .ageRestricted(node.path("age_limit").asInt(0) > 0)
                                .chapters(chapters)
                                .formats(formats)
                                .build();
                        return Mono.just(metadata);
                    } catch (Exception e) {
                        log.error("Failed to parse yt-dlp deep metadata JSON", e);
                        return Mono.error(new RuntimeException("Failed to parse deep metadata", e));
                    }
                });
    }

    private List<String> buildArgs() {
        List<String> args = new ArrayList<>();
        args.add(properties.getBinaryPath());
        args.add("--dump-json");

        if (flatPlaylist) {
            args.add("--flat-playlist");
        }

        // Ensure that ytsearch natively utilizes max-downloads to restrict limits
        // appropriately when executed
        if (maxResults != null && maxResults > 0) {
            if (!target.startsWith("ytsearch")) {
                args.add("--max-downloads");
                args.add(String.valueOf(maxResults));
            }
        }

        // Handle ytsearch limits injection if standard URL was passed as
        // "ytsearch:query"
        String finalTarget = target;
        if (target.startsWith("ytsearch:") && maxResults != null && maxResults > 0) {
            finalTarget = "ytsearch" + maxResults + ":" + target.substring(9);
        }

        args.add(finalTarget);
        return args;
    }
}
