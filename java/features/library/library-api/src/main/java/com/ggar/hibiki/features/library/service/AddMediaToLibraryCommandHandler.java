package com.ggar.hibiki.features.library.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.library.dto.AddMediaToLibraryCommand;
import com.ggar.hibiki.features.library.model.LibraryItem;

/**
 * Service for adding media items (Songs or Albums) to the user's library.
 */
public interface AddMediaToLibraryCommandHandler extends CommandHandler<AddMediaToLibraryCommand, LibraryItem> {}
