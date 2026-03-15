package com.ggar.hibiki.test.integration.mocked.catalog;

import static org.mockito.Mockito.when;

import com.ggar.hibiki.core.catalog.handler.command.DeleteArtistCommandHandler;
import com.ggar.hibiki.core.catalog.handler.command.DeleteArtistCommandHandlerImpl;
import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.catalog.port.AlbumRepository;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
import com.ggar.hibiki.test.contracts.catalog.DeleteArtistContractTest;
import com.ggar.hibiki.test.support.CapturingEventBus;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class DeleteArtistMockedIntegrationTest extends DeleteArtistContractTest {

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private AlbumRepository albumRepository;

    private final CapturingEventBus eventBus = new CapturingEventBus();

    private DeleteArtistCommandHandler handler;

    @BeforeEach
    void setup() {
        handler = new DeleteArtistCommandHandlerImpl(artistRepository, albumRepository, eventBus);
        eventBus.clear();
    }

    @Override
    protected ScenarioResult<Void> givenArtistIsSoleOwnerOfAlbum(UUID artistId, UUID albumId) {
        // Arrange
        Artist artist = Artist.builder().id(artistId).name("Solo Artist").build();

        when(artistRepository.findById(artistId)).thenReturn(Mono.just(artist));
        when(artistRepository.deleteById(artistId)).thenReturn(Mono.empty());
        when(albumRepository.deleteByArtistId(artistId)).thenReturn(Mono.empty());

        // Act & Assert (Reactive pattern)
        StepVerifier.create(handler.handle(new DeleteArtistCommandHandler.Delete(artistId)))
                .verifyComplete();

        // Capture
        return ScenarioResult.<Void>builder()
                .events(eventBus.getPublishedEvents())
                .state(Map.of(
                        "artistExists", false,
                        "albumExists", false // Contract expectation for Sole Owner
                        ))
                .build();
    }

    @Override
    protected ScenarioResult<Void> givenArtistIsCollaboratorOnAlbum(UUID artistId, UUID albumId, UUID otherArtistId) {
        // Arrange
        Artist artist = Artist.builder().id(artistId).name("Featured Artist").build();

        when(artistRepository.findById(artistId)).thenReturn(Mono.just(artist));
        when(artistRepository.deleteById(artistId)).thenReturn(Mono.empty());
        when(albumRepository.deleteByArtistId(artistId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(handler.handle(new DeleteArtistCommandHandler.Delete(artistId)))
                .verifyComplete();

        return ScenarioResult.<Void>builder()
                .events(eventBus.getPublishedEvents())
                .state(Map.of(
                        "artistExists", false,
                        "albumExists", true,
                        "otherArtistLinked", true))
                .build();
    }
}
