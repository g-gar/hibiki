package com.ggar.hibiki.features.library.service.factory;

import com.ggar.hibiki.features.library.model.LibraryItem;
import com.ggar.hibiki.features.library.model.LibraryItemType;
import com.ggar.hibiki.features.library.model.User;
import java.util.UUID;

/**
 * Strategy interface for creating specific types of LibraryItems.
 */
public interface LibraryItemCreator {

    /**
     * Supports the given item type.
     */
    boolean supports(LibraryItemType type);

    /**
     * Creates a new LibraryItem for the given user and media ID.
     */
    LibraryItem create(User user, UUID mediaId);
}
