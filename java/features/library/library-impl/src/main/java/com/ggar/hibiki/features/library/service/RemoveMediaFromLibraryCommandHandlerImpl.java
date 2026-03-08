package com.ggar.hibiki.features.library.service;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.library.dto.RemoveMediaFromLibraryCommand;
import com.ggar.hibiki.features.library.event.MediaRemovedFromLibraryEvent;
import com.ggar.hibiki.features.library.model.User;
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
    private final EventBus eventbus;

    @Override
    public Mono<UUID> handle(RemoveMediaFromLibraryCommand command) {
        User user = command.getIdentityContext().getUser();
        UUID mediaId = command.getMediaId();

        return libraryRepository
                .remove(user, mediaId)
                .then(eventbus.publish(new MediaRemovedFromLibraryEvent(user.getId(), mediaId)))
                .thenReturn(mediaId);
    }
}
