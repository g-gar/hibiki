package com.ggar.hibiki.features.library.handler.command;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.library.model.User;
import com.ggar.hibiki.features.library.model.UserId;
import com.ggar.hibiki.features.library.port.LibraryRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of RemoveMediaFromLibraryCommandHandler.
 */
@Service
@RequiredArgsConstructor
public class RemoveMediaFromLibraryCommandHandlerImpl implements RemoveMediaFromLibraryCommandHandler {

    private final LibraryRepository libraryRepository;
    private final EventBus eventBus;

    @Override
    public Mono<UUID> handle(RemoveMediaFromLibraryCommandHandler.Remove command) {
        User user = User.builder().id(UserId.of(command.userId())).build();
        UUID mediaId = command.mediaId();

        return libraryRepository
                .remove(user, mediaId)
                .then(eventBus.publish(new RemoveMediaFromLibraryCommandHandler.MediaRemoved(user.getId(), mediaId)))
                .thenReturn(mediaId);
    }
}
