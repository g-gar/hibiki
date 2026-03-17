package com.ggar.hibiki.test.integration.mocked.catalog;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ggar.hibiki.core.catalog.handler.command.CreateArtistCommandHandler;
import com.ggar.hibiki.core.catalog.handler.command.CreateArtistCommandHandlerImpl;
import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
import com.ggar.hibiki.test.contracts.catalog.CreateArtistContractTest;
import com.ggar.hibiki.test.support.CapturingEventBus;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class CreateArtistMockedIntegrationTest extends CreateArtistContractTest {

    @Mock
    private ArtistRepository artistRepository;

    private final CapturingEventBus eventBus = new CapturingEventBus();
    private CreateArtistCommandHandler handler;

    @BeforeEach
    void setup() {
        handler = new CreateArtistCommandHandlerImpl(artistRepository, eventBus);
        eventBus.clear();
    }

    @Override
    protected CreateArtistCommandHandler getHandler() {
        return handler;
    }

    @Override
    protected void setupArtistDoesNotExist(String name) {
        UUID generatedId = UUID.randomUUID();
        Artist savedArtist = Artist.builder().id(generatedId).name(name).build();

        when(artistRepository.findByName(name)).thenReturn(Mono.empty());
        when(artistRepository.save(any(Artist.class))).thenReturn(Mono.just(savedArtist));
    }

    @Override
    protected void setupArtistAlreadyExists(String name) {
        UUID existingId = UUID.randomUUID();
        Artist existingArtist = Artist.builder().id(existingId).name(name).build();

        when(artistRepository.findByName(name)).thenReturn(Mono.just(existingArtist));
    }

    @Override
    protected void verifyArtistWasPersisted(String name) {
        verify(artistRepository).save(any(Artist.class));
    }

    @Override
    protected void verifyArtistWasNotPersisted(String name) {
        verify(artistRepository, never()).save(any(Artist.class));
    }
}
