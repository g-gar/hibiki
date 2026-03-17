package com.ggar.hibiki.features.library.factory;

import com.ggar.hibiki.features.library.model.Album;
import com.ggar.hibiki.features.library.model.AlbumId;
import com.ggar.hibiki.features.library.model.AlbumLibraryItem;
import com.ggar.hibiki.features.library.model.LibraryItem;
import com.ggar.hibiki.features.library.model.LibraryItemType;
import com.ggar.hibiki.features.library.model.User;
import com.ggar.hibiki.features.library.model.Visibility;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Creator for AlbumLibraryItem.
 */
@Component
public class AlbumLibraryItemCreator implements LibraryItemCreator {

    @Override
    public boolean supports(LibraryItemType type) {
        return type == LibraryItemType.ALBUM;
    }

    @Override
    public LibraryItem create(User user, UUID mediaId) {
        return AlbumLibraryItem.builder()
                .user(user)
                .album(Album.builder().id(AlbumId.of(mediaId)).build())
                .visibility(Visibility.PRIVATE)
                .owner(true)
                .addedAt(Instant.now())
                .build();
    }
}
