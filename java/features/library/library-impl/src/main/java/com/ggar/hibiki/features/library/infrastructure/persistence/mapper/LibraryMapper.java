package com.ggar.hibiki.features.library.infrastructure.persistence.mapper;

import com.ggar.hibiki.features.library.infrastructure.persistence.entity.AlbumEntity;
import com.ggar.hibiki.features.library.infrastructure.persistence.entity.AlbumLibraryItemEntity;
import com.ggar.hibiki.features.library.infrastructure.persistence.entity.ArtistEntity;
import com.ggar.hibiki.features.library.infrastructure.persistence.entity.LibraryItemEntity;
import com.ggar.hibiki.features.library.infrastructure.persistence.entity.PlaylistEntity;
import com.ggar.hibiki.features.library.infrastructure.persistence.entity.PlaylistItemRelationship;
import com.ggar.hibiki.features.library.infrastructure.persistence.entity.SongEntity;
import com.ggar.hibiki.features.library.infrastructure.persistence.entity.SongLibraryItemEntity;
import com.ggar.hibiki.features.library.infrastructure.persistence.entity.UserEntity;
import com.ggar.hibiki.features.library.model.Album;
import com.ggar.hibiki.features.library.model.AlbumId;
import com.ggar.hibiki.features.library.model.AlbumLibraryItem;
import com.ggar.hibiki.features.library.model.Artist;
import com.ggar.hibiki.features.library.model.ArtistId;
import com.ggar.hibiki.features.library.model.LibraryItem;
import com.ggar.hibiki.features.library.model.LibraryItemId;
import com.ggar.hibiki.features.library.model.Playlist;
import com.ggar.hibiki.features.library.model.PlaylistItem;
import com.ggar.hibiki.features.library.model.PlaylistItemId;
import com.ggar.hibiki.features.library.model.Song;
import com.ggar.hibiki.features.library.model.SongId;
import com.ggar.hibiki.features.library.model.SongLibraryItem;
import com.ggar.hibiki.features.library.model.User;
import com.ggar.hibiki.features.library.model.UserId;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LibraryMapper {

    // --- Domain to Entity ---

    default LibraryItemEntity toEntity(LibraryItem domain) {
        if (domain instanceof SongLibraryItem) {
            return toEntity((SongLibraryItem) domain);
        } else if (domain instanceof AlbumLibraryItem) {
            return toEntity((AlbumLibraryItem) domain);
        } else if (domain instanceof Playlist) {
            return toEntity((Playlist) domain);
        }
        return null;
    }

    SongLibraryItemEntity toEntity(SongLibraryItem domain);

    AlbumLibraryItemEntity toEntity(AlbumLibraryItem domain);

    @Mapping(target = "items", source = "items")
    PlaylistEntity toEntity(Playlist domain);

    @Mapping(target = "song", source = "song")
    PlaylistItemRelationship toRelationship(PlaylistItem domain);

    UserEntity toEntity(User domain);

    SongEntity toEntity(Song domain);

    AlbumEntity toEntity(Album domain);

    ArtistEntity toEntity(Artist domain);

    // --- Entity to Domain ---

    default LibraryItem toDomain(LibraryItemEntity entity) {
        if (entity instanceof SongLibraryItemEntity) {
            return toDomain((SongLibraryItemEntity) entity);
        } else if (entity instanceof AlbumLibraryItemEntity) {
            return toDomain((AlbumLibraryItemEntity) entity);
        } else if (entity instanceof PlaylistEntity) {
            return toDomain((PlaylistEntity) entity);
        }
        return null;
    }

    SongLibraryItem toDomain(SongLibraryItemEntity entity);

    AlbumLibraryItem toDomain(AlbumLibraryItemEntity entity);

    Playlist toDomain(PlaylistEntity entity);

    @Mapping(target = "song", source = "song")
    PlaylistItem toDomain(PlaylistItemRelationship relationship);

    User toDomain(UserEntity entity);

    Song toDomain(SongEntity entity);

    Album toDomain(AlbumEntity entity);

    Artist toDomain(ArtistEntity entity);

    default UUID map(SongId value) {
        return value != null ? value.getValue() : null;
    }

    default SongId mapToSongId(UUID value) {
        return value != null ? SongId.of(value) : null;
    }

    default UUID map(LibraryItemId value) {
        return value != null ? value.getValue() : null;
    }

    default LibraryItemId mapToLibraryItemId(UUID value) {
        return value != null ? LibraryItemId.of(value) : null;
    }

    default UUID map(UserId value) {
        return value != null ? value.getValue() : null;
    }

    default UserId mapToUserId(UUID value) {
        return value != null ? UserId.of(value) : null;
    }

    default UUID map(AlbumId value) {
        return value != null ? value.getValue() : null;
    }

    default AlbumId mapToAlbumId(UUID value) {
        return value != null ? AlbumId.of(value) : null;
    }

    default UUID map(ArtistId value) {
        return value != null ? value.getValue() : null;
    }

    default ArtistId mapToArtistId(UUID value) {
        return value != null ? ArtistId.of(value) : null;
    }

    default UUID map(PlaylistItemId value) {
        return value != null ? value.getValue() : null;
    }

    default PlaylistItemId mapToPlaylistItemId(UUID value) {
        return value != null ? PlaylistItemId.of(value) : null;
    }
}
