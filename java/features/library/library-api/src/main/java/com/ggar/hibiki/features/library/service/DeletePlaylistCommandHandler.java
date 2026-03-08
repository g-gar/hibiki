package com.ggar.hibiki.features.library.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.library.dto.DeletePlaylistCommand;
import java.util.UUID;

/**
 * Service for deleting user playlists.
 */
public interface DeletePlaylistCommandHandler extends CommandHandler<DeletePlaylistCommand, UUID> {}
