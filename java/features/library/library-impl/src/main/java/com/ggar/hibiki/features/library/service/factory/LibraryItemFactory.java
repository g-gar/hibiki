package com.ggar.hibiki.features.library.service.factory;

import com.ggar.hibiki.features.library.model.LibraryItem;
import com.ggar.hibiki.features.library.model.LibraryItemType;
import com.ggar.hibiki.features.library.model.User;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Factory that selects the appropriate creator for a LibraryItemType.
 */
@Component
@RequiredArgsConstructor
public class LibraryItemFactory {

    private final List<LibraryItemCreator> creators;

    /**
     * Creates a LibraryItem for the given type, user, and media ID.
     */
    public LibraryItem create(LibraryItemType type, User user, UUID mediaId) {
        return creators.stream()
                .filter(creator -> creator.supports(type))
                .findFirst()
                .map(creator -> creator.create(user, mediaId))
                .orElseThrow(() -> new IllegalArgumentException("Unsupported library item type: " + type));
    }
}
