package com.ggar.hibiki.features.library.dto;

import com.ggar.hibiki.features.library.model.LibraryItemType;
import com.ggar.hibiki.features.library.model.Visibility;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class LibraryItemDto {
    UUID id;
    UUID userId;
    LibraryItemType type;
    Visibility visibility;
    boolean owner;
    Instant addedAt;

    // Specialized fields (optional based on type)
    UUID songId;
    UUID albumId;
}
