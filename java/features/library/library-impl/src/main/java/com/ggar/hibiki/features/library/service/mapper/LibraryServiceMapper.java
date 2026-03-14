package com.ggar.hibiki.features.library.service.mapper;

import com.ggar.hibiki.features.library.dto.LibraryItemDto;
import com.ggar.hibiki.features.library.dto.PlaylistDto;
import com.ggar.hibiki.features.library.dto.PlaylistItemDto;
import com.ggar.hibiki.features.library.model.AlbumId;
import com.ggar.hibiki.features.library.model.AlbumLibraryItem;
import com.ggar.hibiki.features.library.model.ArtistId;
import com.ggar.hibiki.features.library.model.LibraryItem;
import com.ggar.hibiki.features.library.model.LibraryItemId;
import com.ggar.hibiki.features.library.model.Playlist;
import com.ggar.hibiki.features.library.model.PlaylistItem;
import com.ggar.hibiki.features.library.model.PlaylistItemId;
import com.ggar.hibiki.features.library.model.SongId;
import com.ggar.hibiki.features.library.model.SongLibraryItem;
import com.ggar.hibiki.features.library.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface LibraryServiceMapper {

    @Mapping(target = "id", source = "id", qualifiedByName = "mapLibraryItemIdToString")
    @Mapping(target = "userId", source = "user", qualifiedByName = "mapUserToUserIdString")
    @Mapping(target = "songId", ignore = true)
    @Mapping(target = "albumId", ignore = true)
    default LibraryItemDto toDto(LibraryItem domain) {
        if (domain instanceof SongLibraryItem) {
            return toDto((SongLibraryItem) domain);
        } else if (domain instanceof AlbumLibraryItem) {
            return toDto((AlbumLibraryItem) domain);
        } else if (domain instanceof Playlist) {
            return toDto((Playlist) domain);
        }
        return null;
    }

    @Mapping(target = "id", source = "id", qualifiedByName = "mapLibraryItemIdToString")
    @Mapping(target = "userId", source = "user", qualifiedByName = "mapUserToUserIdString")
    @Mapping(target = "songId", source = "song.id", qualifiedByName = "mapSongIdToString")
    @Mapping(target = "albumId", ignore = true)
    LibraryItemDto toDto(SongLibraryItem domain);

    @Mapping(target = "id", source = "id", qualifiedByName = "mapLibraryItemIdToString")
    @Mapping(target = "userId", source = "user", qualifiedByName = "mapUserToUserIdString")
    @Mapping(target = "albumId", source = "album.id", qualifiedByName = "mapAlbumIdToString")
    @Mapping(target = "songId", ignore = true)
    LibraryItemDto toDto(AlbumLibraryItem domain);

    @Mapping(target = "id", source = "id", qualifiedByName = "mapLibraryItemIdToString")
    @Mapping(target = "userId", source = "user", qualifiedByName = "mapUserToUserIdString")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "songId", ignore = true)
    @Mapping(target = "albumId", ignore = true)
    PlaylistDto toDto(Playlist domain);

    @Mapping(target = "id", source = "id", qualifiedByName = "mapPlaylistItemIdToString")
    @Mapping(target = "songId", source = "song.id", qualifiedByName = "mapSongIdToString")
    PlaylistItemDto toDto(PlaylistItem domain);

    @Named("mapLibraryItemIdToString")
    default String mapLibraryItemIdToString(LibraryItemId value) {
        return value != null ? value.getValue().toString() : null;
    }

    @Named("mapPlaylistItemIdToString")
    default String mapPlaylistItemIdToString(PlaylistItemId value) {
        return value != null ? value.getValue().toString() : null;
    }

    @Named("mapSongIdToString")
    default String mapSongIdToString(SongId value) {
        return value != null ? value.getValue().toString() : null;
    }

    @Named("mapAlbumIdToString")
    default String mapAlbumIdToString(AlbumId value) {
        return value != null ? value.getValue().toString() : null;
    }

    @Named("mapArtistIdToString")
    default String mapArtistIdToString(ArtistId value) {
        return value != null ? value.getValue().toString() : null;
    }

    @Named("mapUserToUserIdString")
    default String mapUserToUserIdString(User value) {
        return (value != null && value.getId() != null)
                ? value.getId().getValue().toString()
                : null;
    }
}
