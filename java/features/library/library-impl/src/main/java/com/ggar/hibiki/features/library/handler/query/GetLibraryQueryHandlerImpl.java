package com.ggar.hibiki.features.library.handler.query;

import com.ggar.hibiki.features.library.model.LibraryItem;
import com.ggar.hibiki.features.library.model.Page;
import com.ggar.hibiki.features.library.model.User;
import com.ggar.hibiki.features.library.model.UserId;
import com.ggar.hibiki.features.library.port.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of GetLibraryQueryHandler.
 */
@Service
@RequiredArgsConstructor
public class GetLibraryQueryHandlerImpl implements GetLibraryQueryHandler {

    private final LibraryRepository libraryRepository;

    @Override
    public Mono<Page<LibraryItem>> handle(GetLibraryQueryHandler.Get query) {
        User user = User.builder().id(UserId.of(query.userId())).build();

        return libraryRepository
                .findAll(user, query.filter(), query.pagination())
                .collectList()
                .zipWith(libraryRepository.count(user, query.filter()))
                .map(tuple -> Page.<LibraryItem>builder()
                        .items(tuple.getT1())
                        .totalItems(tuple.getT2())
                        .nextCursor(null) // TODO: Implement cursor-based pagination
                        .build());
    }
}
