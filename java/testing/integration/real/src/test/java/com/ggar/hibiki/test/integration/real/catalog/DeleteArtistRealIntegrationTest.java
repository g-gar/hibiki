package com.ggar.hibiki.test.integration.real.catalog;

import com.ggar.hibiki.core.catalog.CatalogModuleConfig;
import com.ggar.hibiki.core.catalog.dto.DeleteArtistCommand;
import com.ggar.hibiki.core.catalog.model.Album;
import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.catalog.port.AlbumRepository;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
import com.ggar.hibiki.core.catalog.usecase.handler.command.DeleteArtistCommandHandler;
import com.ggar.hibiki.test.contracts.catalog.DeleteArtistContractTest;
import com.ggar.hibiki.test.integration.real.TestApplication;
import com.ggar.hibiki.test.support.CapturingEventBus;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import com.ggar.hibiki.test.support.SharedInfrastructure;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;

@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test")
public class DeleteArtistRealIntegrationTest extends DeleteArtistContractTest {

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        SharedInfrastructure.registerProperties(registry);
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
    protected ScenarioResult<Void> givenArtistIsSoleOwnerOfAlbum(UUID artistId, UUID albumId) {
        // Arrange
        Artist artist = Artist.builder().id(artistId).name("Solo Artist").build();
        Artist savedArtist = artistRepository.save(artist).block();
        UUID realArtistId = savedArtist.getId();

        Album album = Album.builder()
                .id(albumId)
                .title("Solo Album")
                .releaseYear(2024)
                .artist(savedArtist)
                .build();
        Album savedAlbum = albumRepository.save(album).block();
        UUID realAlbumId = savedAlbum.getId();

        // Act & Assert
        StepVerifier.create(handler.handle(new DeleteArtistCommand(realArtistId)))
                .verifyComplete();

        // Check effects reactively
        AtomicBoolean artistExists = new AtomicBoolean(true);
        StepVerifier.create(artistRepository.existsById(realArtistId))
                .assertNext(artistExists::set)
                .verifyComplete();

        AtomicBoolean albumExists = new AtomicBoolean(true);
        StepVerifier.create(albumRepository.existsById(realAlbumId))
                .assertNext(albumExists::set)
                .verifyComplete();

        return ScenarioResult.<Void>builder()
                .events(eventBus.getPublishedEvents())
                .state(Map.of(
                        "artistExists", artistExists.get(),
                        "albumExists", albumExists.get()))
                .build();
    }

    @Override
    protected ScenarioResult<Void> givenArtistIsCollaboratorOnAlbum(
            UUID artistId, UUID albumId, UUID otherArtistId) {
        // Arrange
        Artist otherArtist =
                Artist.builder().id(otherArtistId).name("Main Artist").build();
        Artist savedOtherArtist = artistRepository.save(otherArtist).block();
        UUID realOtherArtistId = savedOtherArtist.getId();

        Artist collaborator = Artist.builder().id(artistId).name("Collaborator").build();
        Artist savedCollaborator = artistRepository.save(collaborator).block();
        UUID realCollaboratorId = savedCollaborator.getId();

        Album album = Album.builder()
                .id(albumId)
                .title("Main Album")
                .releaseYear(2024)
                .artist(savedOtherArtist) // Linked to the other artist
                .build();
        Album savedAlbum = albumRepository.save(album).block();
        UUID realAlbumId = savedAlbum.getId();

        // Act & Assert
        StepVerifier.create(handler.handle(new DeleteArtistCommand(realCollaboratorId)))
                .verifyComplete();

        // Check effects reactively
        AtomicBoolean artistExists = new AtomicBoolean(true);
        StepVerifier.create(artistRepository.existsById(realCollaboratorId))
                .assertNext(artistExists::set)
                .verifyComplete();

        AtomicBoolean albumExists = new AtomicBoolean(true);
        StepVerifier.create(albumRepository.existsById(realAlbumId))
                .assertNext(albumExists::set)
                .verifyComplete();

        AtomicReference<Album> reloadedAlbum = new AtomicReference<>();
        StepVerifier.create(albumRepository.findById(realAlbumId))
                .assertNext(reloadedAlbum::set)
                .verifyComplete();

        boolean otherArtistLinked = reloadedAlbum.get() != null
                && reloadedAlbum.get().getArtist().getId().equals(realOtherArtistId);

        return ScenarioResult.<Void>builder()
                .events(eventBus.getPublishedEvents())
                .state(Map.of(
                        "artistExists", artistExists.get(),
                        "albumExists", albumExists.get(),
                        "otherArtistLinked", otherArtistLinked))
                .build();
    }
}
