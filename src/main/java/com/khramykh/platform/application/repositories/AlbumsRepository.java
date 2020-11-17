package com.khramykh.platform.application.repositories;

import com.khramykh.platform.domain.entities.Album;

import java.util.Optional;

public interface AlbumsRepository extends PagingAndSortingRepository<Album, Integer> {
    Optional<Album> findAlbumByNameContaining(String name);
}
