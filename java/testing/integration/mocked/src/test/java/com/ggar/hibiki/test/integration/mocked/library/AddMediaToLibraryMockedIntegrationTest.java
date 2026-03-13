package com.ggar.hibiki.test.integration.mocked.library;

import com.ggar.hibiki.features.library.dto.AddMediaToLibraryCommand;
import com.ggar.hibiki.features.library.dto.LibraryItemDto;
import com.ggar.hibiki.features.library.infrastructure.persistence.entity.LibraryItemEntity;
import com.ggar.hibiki.features.library.infrastructure.persistence.mapper.LibraryMapper;
import com.ggar.hibiki.features.library.infrastructure.persistence.repository.LibraryRepository;
import com.ggar.hibiki.features.library.service.AddMediaToLibraryCommandHandlerImpl;
import com.ggar.hibiki.test.contracts.library.AddMediaToLibraryContractTest;
import com.ggar.hibiki.test.support.ScenarioResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddMediaToLibraryMockedIntegrationTest extends AddMediaToLibraryContractTest {

    @Mock
    private LibraryRepository libraryRepository;

    @Mock
    private LibraryItemFactory itemFactory;

    @Mock
    private LibraryMapper libraryMapper;

    private final CapturingEventBus eventBus = new CapturingEventBus();
    private AddMediaToLibraryCommandHandlerImpl handler;

    @BeforeEach
    void setup() {
        handler = new AddMediaToLibraryCommandHandlerImpl(libraryRepository, itemFactory, libraryMapper, eventBus);
        eventBus.clear();
    }

    @Override
    protected ScenarioResult<LibraryItemDto> givenUserAddsSongToLibrary(String userId, String songId) {
        // Arrange
        LibraryItemEntity entity = new LibraryItemEntity();
        entity.setMediaId(songId);
        entity.setUserId(userId);
        
        LibraryItemDto dto = LibraryItemDto.builder()
                .mediaId(songId)
                .userId(userId)
                .build();

        when(libraryRepository.findByUserIdAndMediaId(userId, songId)).thenReturn(Mono.empty());
        when(libraryRepository.save(any(LibraryItemEntity.class))).thenReturn(Mono.just(entity));
        when(libraryMapper.toDto(any(LibraryItemEntity.class))).thenReturn(dto);

        // Act & Assert
        AtomicReference<LibraryItemDto> responseRef = new AtomicReference<>();
        handler.handle(new AddMediaToLibraryCommand(UUID.fromString(userId), UUID.fromString(songId)))
                .as(StepVerifier::create)
                .assertNext(responseRef::set)
                .verifyComplete();

        return ScenarioResult.<LibraryItemDto>builder()
                .returnValue(responseRef.get())
                .state(Map.of("persisted", true))
                .build();
    }

    @Override
    protected ScenarioResult<LibraryItemDto> givenSongIsAlreadyInLibrary(String userId, String songId) {
        // Arrange
        LibraryItemEntity entity = new LibraryItemEntity();
        entity.setMediaId(songId);
        entity.setUserId(userId);
        
        LibraryItemDto dto = LibraryItemDto.builder()
                .mediaId(songId)
                .userId(userId)
                .build();

        when(libraryRepository.findByUserIdAndMediaId(userId, songId)).thenReturn(Mono.just(entity));
        when(libraryMapper.toDto(any(LibraryItemEntity.class))).thenReturn(dto);

        // Act & Assert
        AtomicReference<LibraryItemDto> responseRef = new AtomicReference<>();
        handler.handle(new AddMediaToLibraryCommand(UUID.fromString(userId), UUID.fromString(songId)))
                .as(StepVerifier::create)
                .assertNext(responseRef::set)
                .verifyComplete();

        verify(libraryRepository, never()).save(any());

        return ScenarioResult.<LibraryItemDto>builder()
                .returnValue(responseRef.get())
                .state(Map.of("countBefore", 1, "countAfter", 1))
                .build();
    }
}
