package com.ggar.hibiki.test.integration.mocked.catalog;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.ggar.hibiki.core.catalog.dto.ArtistDto;
import com.ggar.hibiki.core.catalog.dto.CreateArtistCommand;
import com.ggar.hibiki.core.catalog.persistence.entity.ArtistEntity;
import com.ggar.hibiki.core.catalog.persistence.mapper.ArtistMapper;
import com.ggar.hibiki.core.catalog.persistence.repository.ArtistRepository;
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
        String generatedId = UUID.randomUUID().toString();
        ArtistEntity savedEntity = new ArtistEntity(name);
        savedEntity.setId(generatedId);

        ArtistDto expectedDto = ArtistDto.builder().id(generatedId).name(name).build();

        when(artistRepository.findByNameIgnoreCase(name)).thenReturn(Mono.empty());
        when(artistRepository.save(any(ArtistEntity.class))).thenReturn(Mono.just(savedEntity));
        when(artistMapper.toDto(any(ArtistEntity.class))).thenReturn(expectedDto);

        // Act & Assert (Reactive pattern without .block())
        AtomicReference<ArtistDto> resultRef = new AtomicReference<>();
        handler.handle(new CreateArtistCommand(name))
                .as(StepVerifier::create)
                .assertNext(resultRef::set)
                .verifyComplete();

        // Capture
        verify(artistRepository).save(any(ArtistEntity.class));

        return ScenarioResult.<ArtistDto>builder()
                .returnValue(resultRef.get())
                .state(Map.of("persisted", true))
                .build();
    }

    @Override
    protected ScenarioResult<ArtistDto> givenArtistAlreadyExists(String name) {
        // Arrange
        String existingId = UUID.randomUUID().toString();
        ArtistEntity existingEntity = new ArtistEntity(name);
        existingEntity.setId(existingId);

        ArtistDto existingDto = ArtistDto.builder().id(existingId).name(name).build();

        when(artistRepository.findByNameIgnoreCase(name)).thenReturn(Mono.just(existingEntity));
        when(artistMapper.toDto(any(ArtistEntity.class))).thenReturn(existingDto);

        // Act & Assert
        AtomicReference<ArtistDto> resultRef = new AtomicReference<>();
        handler.handle(new CreateArtistCommand(name))
                .as(StepVerifier::create)
                .assertNext(resultRef::set)
                .verifyComplete();

        // Capture
        verify(artistRepository, never()).save(any(ArtistEntity.class));

        return ScenarioResult.<ArtistDto>builder()
                .returnValue(resultRef.get())
                .state(Map.of("countBefore", 1, "countAfter", 1))
                .build();
    }
}
