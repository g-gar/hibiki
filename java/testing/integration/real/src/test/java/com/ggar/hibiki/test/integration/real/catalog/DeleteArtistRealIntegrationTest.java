package com.ggar.hibiki.test.integration.real.catalog;

import com.ggar.hibiki.core.catalog.dto.DeleteArtistCommand;
import com.ggar.hibiki.core.catalog.persistence.entity.AlbumEntity;
import com.ggar.hibiki.core.catalog.persistence.entity.ArtistEntity;
import com.ggar.hibiki.core.catalog.persistence.repository.AlbumRepository;
import com.ggar.hibiki.core.catalog.persistence.repository.ArtistRepository;
import com.ggar.hibiki.core.catalog.usecase.handler.command.DeleteArtistCommandHandler;
import com.ggar.hibiki.test.contracts.catalog.DeleteArtistContractTest;
import com.ggar.hibiki.test.support.CapturingEventBus;
import com.ggar.hibiki.test.support.ScenarioResult;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
@Testcontainers
public class DeleteArtistRealIntegrationTest extends DeleteArtistContractTest {

    @Container
    static Neo4jContainer<?> neo4j = new Neo4jContainer<>("neo4j:5")
            .withoutAuthentication();

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", neo4j::getBoltUrl);
    }

    @TestConfiguration
    static class Config {
        @Bean
        @Primary
        public CapturingEventBus capturingEventBus() {
            return new CapturingEventBus();
        }
    }

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private DeleteArtistCommandHandler handler;

    @Autowired
    private CapturingEventBus eventBus;

    @BeforeEach
    void clearBus() {
        eventBus.clear();
    }

    @Override
    protected ScenarioResult<Void> givenArtistIsSoleOwnerOfAlbum(String artistId, String albumId) {
        // Arrange
        ArtistEntity artist = new ArtistEntity("Solo Artist");
        artist.setId(artistId);
        artistRepository.save(artist)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        AlbumEntity album = new AlbumEntity("Solo Album", 2024);
        album.setId(albumId);
        album.setArtist(artist);
        albumRepository.save(album)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        // Act & Assert
        handler.handle(new DeleteArtistCommand(artistId))
                .as(StepVerifier::create)
                .verifyComplete();

        // Check effects reactively
        AtomicBoolean artistExists = new AtomicBoolean(true);
        artistRepository.existsById(artistId)
                .as(StepVerifier::create)
                .assertNext(artistExists::set)
                .verifyComplete();

        AtomicBoolean albumExists = new AtomicBoolean(true);
        albumRepository.existsById(albumId)
                .as(StepVerifier::create)
                .assertNext(albumExists::set)
                .verifyComplete();

        return ScenarioResult.<Void>builder()
                .events(eventBus.getPublishedEvents())
                .state(Map.of(
                    "artistExists", artistExists.get(),
                    "albumExists", albumExists.get()
                ))
                .build();
    }

    @Override
    protected ScenarioResult<Void> givenArtistIsCollaboratorOnAlbum(String artistId, String albumId, String otherArtistId) {
        // Arrange
        ArtistEntity mainArtist = new ArtistEntity("Main Artist");
        mainArtist.setId(otherArtistId);
        artistRepository.save(mainArtist)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        ArtistEntity collaborator = new ArtistEntity("Collaborator");
        collaborator.setId(artistId);
        artistRepository.save(collaborator)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        AlbumEntity album = new AlbumEntity("Main Album", 2024);
        album.setId(albumId);
        album.setArtist(mainArtist); 
        albumRepository.save(album)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        // Act & Assert
        handler.handle(new DeleteArtistCommand(artistId))
                .as(StepVerifier::create)
                .verifyComplete();

        // Check effects reactively
        AtomicBoolean artistExists = new AtomicBoolean(true);
        artistRepository.existsById(artistId)
                .as(StepVerifier::create)
                .assertNext(artistExists::set)
                .verifyComplete();

        AtomicBoolean albumExists = new AtomicBoolean(true);
        albumRepository.existsById(albumId)
                .as(StepVerifier::create)
                .assertNext(albumExists::set)
                .verifyComplete();
        
        AtomicReference<AlbumEntity> reloadedAlbum = new AtomicReference<>();
        albumRepository.findById(albumId)
                .as(StepVerifier::create)
                .assertNext(reloadedAlbum::set)
                .verifyComplete();

        boolean otherArtistLinked = reloadedAlbum.get() != null && reloadedAlbum.get().getArtist().getId().equals(otherArtistId);

        return ScenarioResult.<Void>builder()
                .events(eventBus.getPublishedEvents())
                .state(Map.of(
                    "artistExists", artistExists.get(),
                    "albumExists", albumExists.get(),
                    "otherArtistLinked", otherArtistLinked
                ))
                .build();
    }
}
