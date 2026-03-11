package com.ggar.hibiki.features.metadata.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.metadata.dto.MapToId3Command;
import com.ggar.hibiki.features.metadata.model.Id3Result;
import com.ggar.hibiki.packages.id3v2.model.Id3v2Frame;
import com.ggar.hibiki.packages.id3v2.model.Id3v2Tag;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import java.io.InputStream;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class MapToId3CommandHandlerImpl implements CommandHandler<MapToId3Command, Id3Result> {

    private final Map<String, String> id3Mappings;
    private final ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    public MapToId3CommandHandlerImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        Yaml yaml = new Yaml();
        try (InputStream in = getClass().getResourceAsStream("/mapping/musicbrainz_to_id3.yml")) {
            Map<String, Object> config = yaml.load(in);
            this.id3Mappings = (Map<String, String>) config.get("mapping");
            log.info("Loaded {} ID3 mappings from musicbrainz_to_id3.yml", id3Mappings.size());
        } catch (Exception e) {
            log.error("Failed to load musicbrainz_to_id3.yml mapping file", e);
            throw new RuntimeException("Could not initialize ID3 mapping configuration", e);
        }
    }

    @Override
    public Mono<Id3Result> handle(MapToId3Command command) {
        return Mono.fromCallable(() -> {
            Id3v2Tag id3Tag = new Id3v2Tag();

            // Convert IsrcResponse to a JSON string or Map so JsonPath can read it
            // We serialize it first as JsonPath works best with raw JSON strings or Map graphs
            String jsonPayload = objectMapper.writeValueAsString(command.getRawMetadata());
            Object document =
                    Configuration.defaultConfiguration().jsonProvider().parse(jsonPayload);

            for (Map.Entry<String, String> entry : id3Mappings.entrySet()) {
                String frameId = entry.getKey();
                String jsonPathExp = entry.getValue();

                try {
                    String extractedValue = JsonPath.read(document, jsonPathExp);
                    if (extractedValue != null && !extractedValue.isBlank()) {
                        Id3v2Frame frame = new Id3v2Frame();
                        frame.setId(frameId);
                        frame.setData(extractedValue.getBytes());
                        id3Tag.addFrame(frame);
                        log.debug("Mapped ID3 frame {} -> {}", frameId, extractedValue);
                    }
                } catch (PathNotFoundException e) {
                    // It's normal if some paths are not found (e.g. no release events)
                    log.trace("JsonPath not found for frame {}: {}", frameId, jsonPathExp);
                } catch (Exception e) {
                    log.warn("Error extracting JsonPath for frame {}: {}", frameId, e.getMessage());
                }
            }

            return Id3Result.builder()
                    .mediaId(command.getMediaId())
                    .tags(id3Tag)
                    .build();
        });
    }
}
