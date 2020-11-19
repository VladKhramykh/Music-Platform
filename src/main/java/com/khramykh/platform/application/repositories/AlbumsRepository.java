package com.khramykh.platform.application.repositories;

import com.khramykh.platform.domain.entities.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface AlbumsRepository extends PagingAndSortingRepository<Album, Integer> {
    Page<Album> findAlbumByNameContaining(String name, Pageable page);
}
