package com.ggar.hibiki.features.library.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.library.dto.RemoveMediaFromLibraryCommand;
import java.util.UUID;

/**
 * Service for removing media items from the user's library.
 */
public interface RemoveMediaFromLibraryCommandHandler extends CommandHandler<RemoveMediaFromLibraryCommand, UUID> {}
