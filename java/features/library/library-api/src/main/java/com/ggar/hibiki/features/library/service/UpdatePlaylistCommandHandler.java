package com.ggar.hibiki.features.library.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.library.dto.UpdatePlaylistCommand;
import com.ggar.hibiki.features.library.model.Playlist;

/**
 * Service for performing batch operations on user playlists.
 */
public interface UpdatePlaylistCommandHandler extends CommandHandler<UpdatePlaylistCommand, Playlist> {}
