package com.ggar.hibiki.features.library.service;

import com.ggar.hibiki.features.library.dto.GetLibraryQuery;
import com.ggar.hibiki.features.library.dto.LibraryItemDto;
import com.ggar.hibiki.features.library.infrastructure.persistence.mapper.LibraryMapper;
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
    private final LibraryMapper libraryMapper;

    @Override
    public Mono<Page<LibraryItemDto>> handle(GetLibraryQuery query) {
        User user = User.builder().id(UserId.of(query.getUserId())).build();

        return libraryRepository
                .findAll(user, query.getFilter(), query.getPagination())
                .map(libraryMapper::toDto)
                .collectList()
                .zipWith(libraryRepository.count(user, query.getFilter()))
                .map(tuple -> Page.<LibraryItemDto>builder()
                        .items(tuple.getT1())
                        .totalItems(tuple.getT2())
                        .nextCursor(null) // TODO: Implement cursor-based pagination
                        .build());
    }
}
