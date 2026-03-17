package com.ggar.hibiki.features.library.handler.query;

import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import com.ggar.hibiki.features.library.model.LibraryFilter;
import com.ggar.hibiki.features.library.model.LibraryItem;
import com.ggar.hibiki.features.library.model.Page;
import com.ggar.hibiki.features.library.model.Pagination;
import java.util.UUID;

public interface GetLibraryQueryHandler extends QueryHandler<GetLibraryQueryHandler.Get, Page<LibraryItem>> {

    record Get(UUID userId, LibraryFilter filter, Pagination pagination) implements Query<Page<LibraryItem>> {}
}
