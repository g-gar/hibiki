package com.ggar.hibiki.features.library.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

/**
 * Represents a Song saved in the user's library.
 */
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class SongLibraryItem extends LibraryItem {
    /**
     * The Song referenced by this library entry.
     */
    Song song;

    @Override
    public LibraryItemType getType() {
        return LibraryItemType.SONG;
    }
}
