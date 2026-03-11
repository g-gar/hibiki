package com.ggar.hibiki.features.metadata.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggar.hibiki.features.metadata.dto.MapToId3Command;
import com.ggar.hibiki.features.metadata.model.Id3Result;
import com.ggar.hibiki.features.metadata.model.MediaId;
import com.ggar.hibiki.packages.id3v2.model.Id3v2Frame;
import com.ggar.hibiki.packages.id3v2.model.Id3v2Tag;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class MapToId3CommandHandlerImpl implements MapToId3CommandHandler {

    private final Map<String, String> mappings;
    private final Configuration jsonPathConfig;

    public MapToId3CommandHandlerImpl(ObjectMapper objectMapper) {
        this.jsonPathConfig = Configuration.builder()
                .mappingProvider(new JacksonMappingProvider(objectMapper))
                .jsonProvider(new JacksonJsonProvider(objectMapper))
                .options(Option.SUPPRESS_EXCEPTIONS)
                .build();
        
        try (InputStream is = getClass().getResourceAsStream("/mapping/musicbrainz_to_id3.yml")) {
            if (is == null) {
                throw new IllegalStateException("Mapping file not found: /mapping/musicbrainz_to_id3.yml");
            }
            Yaml yaml = new Yaml();
            Map<String, Map<String, String>> yamlMap = yaml.load(is);
            this.mappings = yamlMap.get("mapping");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load ID3 mapping", e);
        }
    }

    @Override
    public Publisher<Id3Result> handle(MapToId3Command command) {
        log.info("Mapping metadata to ID3 tags for mediaId: {}", command.getMediaId());

        return Mono.fromCallable(() -> {
            Id3v2Tag tag = new Id3v2Tag();

            if (command.getRawMetadata() == null) {
                return Id3Result.builder()
                        .mediaId(MediaId.of(command.getMediaId()))
                        .tags(tag)
                        .build();
            }

            DocumentContext context = JsonPath.using(jsonPathConfig).parse(command.getRawMetadata());

            mappings.forEach((frameId, jsonPath) -> {
                try {
                    Object value = context.read(jsonPath);
                    if (value != null) {
                        String textValue = String.valueOf(value);
                        if (!textValue.isEmpty()) {
                            tag.addFrame(createTextFrame(frameId, textValue));
                        }
                    }
                } catch (Exception e) {
                    log.debug("Could not resolve path {} for frame {}", jsonPath, frameId, e);
                }
            });

            return Id3Result.builder()
                    .mediaId(MediaId.of(command.getMediaId()))
                    .tags(tag)
                    .build();
        });
    }

    private Id3v2Frame createTextFrame(String frameIdName, String text) {
        // ID3v2 text frames start with an encoding byte.
        // 0x00 = ISO-8859-1. 0x01 = UTF-16. 0x03 = UTF-8.
        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        byte[] data = new byte[textBytes.length + 1];
        data[0] = 0x03; // UTF-8 encoding flag
        System.arraycopy(textBytes, 0, data, 1, textBytes.length);

        return new Id3v2Frame(
                frameIdName,
                data.length,
                new byte[] {0, 0}, // Flags
                data);
    }
}
