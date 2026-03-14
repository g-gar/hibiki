package com.ggar.hibiki.test.integration.mocked.library;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ggar.hibiki.features.library.dto.RemoveMediaFromLibraryCommand;
import com.ggar.hibiki.features.library.port.LibraryRepository;
import com.ggar.hibiki.features.library.service.RemoveMediaFromLibraryCommandHandlerImpl;
import com.ggar.hibiki.test.support.CapturingEventBus;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class RemoveMediaFromLibraryMockedIntegrationTest {

    @Mock
    private LibraryRepository libraryRepository;

    private final CapturingEventBus eventBus = new CapturingEventBus();
    private RemoveMediaFromLibraryCommandHandlerImpl handler;

    @BeforeEach
    void setup() {
        handler = new RemoveMediaFromLibraryCommandHandlerImpl(libraryRepository, eventBus);
        eventBus.clear();
    }

    @Test
    @DisplayName("Scenario: remove song from library")
    void shouldRemoveSong() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID mediaId = UUID.randomUUID();
        when(libraryRepository.remove(any(), eq(mediaId))).thenReturn(Mono.empty());

        // Act & Assert
        AtomicReference<UUID> resultRef = new AtomicReference<>();
        handler.handle(RemoveMediaFromLibraryCommand.builder()
                        .userId(userId)
                        .type(com.ggar.hibiki.features.library.model.LibraryItemType.SONG)
                        .mediaId(mediaId)
                        .build())
                .as(StepVerifier::create)
                .assertNext(resultRef::set)
                .verifyComplete();

        // Capture/Verify
        assertThat(resultRef.get()).isEqualTo(mediaId);
        assertThat(eventBus.getPublishedEvents()).hasSize(1);
        verify(libraryRepository).remove(any(), eq(mediaId));
    }

    @Test
    @DisplayName("Scenario: remove non-existent song (idempotent)")
    void shouldBeIdempotent() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID mediaId = UUID.randomUUID();
        when(libraryRepository.remove(any(), eq(mediaId))).thenReturn(Mono.empty());

        // Act & Assert
        // Act & Assert
        handler.handle(RemoveMediaFromLibraryCommand.builder()
                        .userId(userId)
                        .type(com.ggar.hibiki.features.library.model.LibraryItemType.SONG)
                        .mediaId(mediaId)
                        .build())
                .as(StepVerifier::create)
                .expectNext(mediaId)
                .verifyComplete();

        verify(libraryRepository).remove(any(), eq(mediaId));
    }
}
