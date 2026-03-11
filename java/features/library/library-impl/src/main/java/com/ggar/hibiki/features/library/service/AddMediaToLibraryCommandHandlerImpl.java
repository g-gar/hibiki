package com.ggar.hibiki.features.library.service;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.library.dto.AddMediaToLibraryCommand;
import com.ggar.hibiki.features.library.event.MediaAddedToLibraryEvent;
import com.ggar.hibiki.features.library.model.LibraryItem;
import com.ggar.hibiki.features.library.model.User;
import com.ggar.hibiki.features.library.model.UserId;
import com.ggar.hibiki.features.library.port.LibraryRepository;
import com.ggar.hibiki.features.library.service.factory.LibraryItemFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of AddMediaToLibraryCommandHandler.
 */
@Service
@RequiredArgsConstructor
public class AddMediaToLibraryCommandHandlerImpl implements AddMediaToLibraryCommandHandler {

    private final LibraryRepository libraryRepository;
    private final LibraryItemFactory itemFactory;
    private final EventBus eventBus;

    @Override
    public Mono<LibraryItem> handle(AddMediaToLibraryCommand command) {
        User user = User.builder().id(UserId.of(command.getUserId())).build();

        return libraryRepository
                .findById(user, command.getMediaId())
                .switchIfEmpty(Mono.defer(() -> {
                    LibraryItem item = itemFactory.create(command.getType(), user, command.getMediaId());
                    return libraryRepository.save(user, item);
                }))
                .flatMap(item -> eventBus.publish(MediaAddedToLibraryEvent.builder()
                                .userId(user.getId())
                                .mediaId(command.getMediaId())
                                .type(command.getType())
                                .build())
                        .thenReturn(item));
    }
}
