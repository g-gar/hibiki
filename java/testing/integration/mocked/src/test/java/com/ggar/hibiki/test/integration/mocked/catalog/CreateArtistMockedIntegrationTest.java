package com.ggar.hibiki.test.integration.mocked.catalog;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ggar.hibiki.core.catalog.dto.ArtistDto;
import com.ggar.hibiki.core.catalog.dto.CreateArtistCommand;
import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.catalog.persistence.mapper.ArtistMapper;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
import com.ggar.hibiki.core.catalog.usecase.handler.command.CreateArtistCommandHandler;
import com.ggar.hibiki.test.contracts.catalog.CreateArtistContractTest;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class CreateArtistMockedIntegrationTest extends CreateArtistContractTest {

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private ArtistMapper artistMapper;

    @InjectMocks
    private CreateArtistCommandHandler handler;

    @Override
    protected ScenarioResult<ArtistDto> givenArtistDoesNotExist(String name) {
        // Arrange
        UUID generatedId = UUID.randomUUID();
        Artist savedArtist = Artist.builder().id(generatedId).name(name).build();

        ArtistDto expectedDto = ArtistDto.builder().id(generatedId).name(name).build();

        when(artistRepository.findByName(name)).thenReturn(Mono.empty());
        when(artistRepository.save(any(Artist.class))).thenReturn(Mono.just(savedArtist));
        when(artistMapper.toDto(any(Artist.class))).thenReturn(expectedDto);

        // Act & Assert (Reactive pattern without .block())
        AtomicReference<ArtistDto> resultRef = new AtomicReference<>();
        handler.handle(new CreateArtistCommand(name))
                .as(StepVerifier::create)
                .assertNext(resultRef::set)
                .verifyComplete();

        // Capture
        verify(artistRepository).save(any(Artist.class));

        return ScenarioResult.<ArtistDto>builder()
                .returnValue(resultRef.get())
                .state(Map.of("persisted", true))
                .build();
    }

    @Override
    protected ScenarioResult<ArtistDto> givenArtistAlreadyExists(String name) {
        // Arrange
        UUID existingId = UUID.randomUUID();
        Artist existingArtist = Artist.builder().id(existingId).name(name).build();

        ArtistDto existingDto = ArtistDto.builder().id(existingId).name(name).build();

        when(artistRepository.findByName(name)).thenReturn(Mono.just(existingArtist));
        when(artistMapper.toDto(any(Artist.class))).thenReturn(existingDto);

        // Act & Assert
        AtomicReference<ArtistDto> resultRef = new AtomicReference<>();
        handler.handle(new CreateArtistCommand(name))
                .as(StepVerifier::create)
                .assertNext(resultRef::set)
                .verifyComplete();

        // Capture
        verify(artistRepository, never()).save(any(Artist.class));

        return ScenarioResult.<ArtistDto>builder()
                .returnValue(resultRef.get())
                .state(Map.of("countBefore", 1, "countAfter", 1))
                .build();
    }
}
