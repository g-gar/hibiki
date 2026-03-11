package com.ggar.hibiki.features.library.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.library.dto.CreatePlaylistCommand;
import com.ggar.hibiki.features.library.dto.PlaylistDto;

/**
 * Service for creating new user playlists.
 */
public interface CreatePlaylistCommandHandler extends CommandHandler<CreatePlaylistCommand, PlaylistDto> {}
