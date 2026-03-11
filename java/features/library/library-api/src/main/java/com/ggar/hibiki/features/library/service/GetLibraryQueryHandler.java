package com.ggar.hibiki.features.library.service;

import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import com.ggar.hibiki.features.library.dto.GetLibraryQuery;
import com.ggar.hibiki.features.library.dto.LibraryItemDto;
import com.ggar.hibiki.features.library.model.Page;

/**
 * Service for retrieving items from the user's library using filters and pagination.
 */
public interface GetLibraryQueryHandler extends QueryHandler<GetLibraryQuery, Page<LibraryItemDto>> {}
