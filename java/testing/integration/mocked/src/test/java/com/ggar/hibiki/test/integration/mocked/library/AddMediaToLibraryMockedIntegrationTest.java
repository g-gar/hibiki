package com.ggar.hibiki.test.integration.mocked.library;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ggar.hibiki.features.library.factory.LibraryItemFactory;
import com.ggar.hibiki.features.library.handler.command.AddMediaToLibraryCommandHandler;
import com.ggar.hibiki.features.library.handler.command.AddMediaToLibraryCommandHandlerImpl;
import com.ggar.hibiki.features.library.model.LibraryItem;
import com.ggar.hibiki.features.library.model.LibraryItemType;
import com.ggar.hibiki.features.library.model.Song;
import com.ggar.hibiki.features.library.model.SongId;
import com.ggar.hibiki.features.library.model.SongLibraryItem;
import com.ggar.hibiki.features.library.model.User;
import com.ggar.hibiki.features.library.port.LibraryRepository;
import com.ggar.hibiki.test.contracts.library.AddMediaToLibraryContractTest;
import com.ggar.hibiki.test.support.CapturingEventBus;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class AddMediaToLibraryMockedIntegrationTest extends AddMediaToLibraryContractTest {

    @Mock
    private LibraryRepository libraryRepository;

    @Mock
    private LibraryItemFactory itemFactory;

    private final CapturingEventBus eventBus = new CapturingEventBus();
    private AddMediaToLibraryCommandHandlerImpl handler;

    @BeforeEach
    void setup() {
        handler = new AddMediaToLibraryCommandHandlerImpl(libraryRepository, itemFactory, eventBus);
        eventBus.clear();
    }

    @Override
    protected ScenarioResult<LibraryItem> givenUserAddsSongToLibrary(UUID userId, UUID songId) {
        // Arrange
        LibraryItem item = mock(SongLibraryItem.class);

        Song song = Song.builder().id(SongId.of(songId)).build();
        when(((SongLibraryItem) item).getSong()).thenReturn(song);

        when(libraryRepository.findById(any(User.class), eq(songId))).thenReturn(Mono.empty());
        when(itemFactory.create(eq(LibraryItemType.SONG), any(User.class), eq(songId)))
                .thenReturn(item);
        when(libraryRepository.save(any(User.class), eq(item))).thenReturn(Mono.just(item));

        // Act & Assert
        AtomicReference<LibraryItem> responseRef = new AtomicReference<>();
        StepVerifier.create(
                        handler.handle(new AddMediaToLibraryCommandHandler.Add(userId, LibraryItemType.SONG, songId)))
                .assertNext(responseRef::set)
                .verifyComplete();

        return ScenarioResult.<LibraryItem>builder()
                .returnValue(responseRef.get())
                .state(Map.of("persisted", true))
                .build();
    }

    @Override
    protected ScenarioResult<LibraryItem> givenSongIsAlreadyInLibrary(UUID userId, UUID songId) {
        // Arrange
        LibraryItem item = mock(SongLibraryItem.class);

        Song song = Song.builder().id(SongId.of(songId)).build();
        when(((SongLibraryItem) item).getSong()).thenReturn(song);

        when(libraryRepository.findById(any(User.class), eq(songId))).thenReturn(Mono.just(item));

        // Act & Assert
        AtomicReference<LibraryItem> responseRef = new AtomicReference<>();
        StepVerifier.create(
                        handler.handle(new AddMediaToLibraryCommandHandler.Add(userId, LibraryItemType.SONG, songId)))
                .assertNext(responseRef::set)
                .verifyComplete();

        verify(libraryRepository, never()).save(any(), any());

        return ScenarioResult.<LibraryItem>builder()
                .returnValue(responseRef.get())
                .state(Map.of("countBefore", 1, "countAfter", 1))
                .build();
    }
}
