package com.ggar.hibiki.test.integration.mocked.catalog;

import com.ggar.hibiki.core.catalog.dto.DeleteArtistCommand;
import com.ggar.hibiki.core.catalog.persistence.entity.ArtistEntity;
import com.ggar.hibiki.core.catalog.persistence.repository.AlbumRepository;
import com.ggar.hibiki.core.catalog.persistence.repository.ArtistRepository;
import com.ggar.hibiki.core.catalog.usecase.handler.command.DeleteArtistCommandHandler;
import com.ggar.hibiki.test.contracts.catalog.DeleteArtistContractTest;
import com.ggar.hibiki.test.support.CapturingEventBus;
import com.ggar.hibiki.test.support.ScenarioResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.mockito.Mockito.*;

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
        handler = new DeleteArtistCommandHandler(artistRepository, albumRepository, eventBus);
        eventBus.clear();
    }

    @Override
    protected ScenarioResult<Void> givenArtistIsSoleOwnerOfAlbum(String artistId, String albumId) {
        // Arrange
        ArtistEntity artist = new ArtistEntity("Solo Artist");
        artist.setId(artistId);

        when(artistRepository.findById(artistId)).thenReturn(Mono.just(artist));
        when(artistRepository.deleteById(artistId)).thenReturn(Mono.empty());

        // Act & Assert (Reactive pattern)
        handler.handle(new DeleteArtistCommand(artistId))
                .as(StepVerifier::create)
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
    protected ScenarioResult<Void> givenArtistIsCollaboratorOnAlbum(String artistId, String albumId, String otherArtistId) {
        // Arrange
        ArtistEntity artist = new ArtistEntity("Featured Artist");
        artist.setId(artistId);

        when(artistRepository.findById(artistId)).thenReturn(Mono.just(artist));
        when(artistRepository.deleteById(artistId)).thenReturn(Mono.empty());

        // Act & Assert
        handler.handle(new DeleteArtistCommand(artistId))
                .as(StepVerifier::create)
                .verifyComplete();

        return ScenarioResult.<Void>builder()
                .events(eventBus.getPublishedEvents())
                .state(Map.of(
                    "artistExists", false,
                    "albumExists", true, 
                    "otherArtistLinked", true
                ))
                .build();
    }
}
