package com.ggar.hibiki.features.library.service.factory;

import com.ggar.hibiki.features.library.model.LibraryItem;
import com.ggar.hibiki.features.library.model.LibraryItemType;
import com.ggar.hibiki.features.library.model.Song;
import com.ggar.hibiki.features.library.model.SongLibraryItem;
import com.ggar.hibiki.features.library.model.User;
import com.ggar.hibiki.features.library.model.Visibility;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Creator for SongLibraryItem.
 */
@Component
public class SongLibraryItemCreator implements LibraryItemCreator {

    @Override
    public boolean supports(LibraryItemType type) {
        return type == LibraryItemType.SONG;
    }

    @Override
    public LibraryItem create(User user, UUID mediaId) {
        return SongLibraryItem.builder()
                .user(user)
                .song(Song.builder().id(mediaId).build())
                .visibility(Visibility.PRIVATE)
                .owner(true)
                .addedAt(Instant.now())
                .build();
    }
}
