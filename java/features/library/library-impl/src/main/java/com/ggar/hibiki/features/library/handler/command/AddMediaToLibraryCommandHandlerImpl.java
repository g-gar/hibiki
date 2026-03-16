package com.ggar.hibiki.features.library.handler.command;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.library.factory.LibraryItemFactory;
import com.ggar.hibiki.features.library.model.LibraryItem;
import com.ggar.hibiki.features.library.model.User;
import com.ggar.hibiki.features.library.model.UserId;
import com.ggar.hibiki.features.library.port.LibraryRepository;
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
    public Mono<LibraryItem> handle(AddMediaToLibraryCommandHandler.Add command) {
        User user = User.builder().id(UserId.of(command.userId())).build();

        return libraryRepository
                .findById(user, command.mediaId())
                .switchIfEmpty(Mono.defer(() -> {
                    LibraryItem item = itemFactory.create(command.type(), user, command.mediaId());
                    return libraryRepository.save(user, item);
                }))
                .flatMap(item -> eventBus.publish(new AddMediaToLibraryCommandHandler.MediaAdded(
                                user.getId(), command.mediaId(), command.type()))
                        .thenReturn(item));
    }
}
