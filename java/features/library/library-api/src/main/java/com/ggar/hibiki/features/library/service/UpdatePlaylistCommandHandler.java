package com.ggar.hibiki.features.library.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.library.dto.PlaylistDto;
import com.ggar.hibiki.features.library.dto.UpdatePlaylistCommand;

/**
 * Service for performing batch operations on user playlists.
 */
public interface UpdatePlaylistCommandHandler extends CommandHandler<UpdatePlaylistCommand, PlaylistDto> {}
