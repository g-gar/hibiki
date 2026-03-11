package com.ggar.hibiki.features.library.service;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.library.dto.RemoveMediaFromLibraryCommand;
import com.ggar.hibiki.features.library.event.MediaRemovedFromLibraryEvent;
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
    private final EventBus eventbus;

    @Override
    public Mono<UUID> handle(RemoveMediaFromLibraryCommand command) {
        User user = User.builder().id(UserId.of(command.getUserId())).build();
        UUID mediaId = command.getMediaId();

        return libraryRepository
                .remove(user, mediaId)
                .then(eventbus.publish(MediaRemovedFromLibraryEvent.builder()
                        .userId(user.getId())
                        .mediaId(mediaId)
                        .build()))
                .thenReturn(mediaId);
    }
}
