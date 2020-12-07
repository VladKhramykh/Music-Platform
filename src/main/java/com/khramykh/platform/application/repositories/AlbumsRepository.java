package com.khramykh.platform.application.repositories;

import com.khramykh.platform.domain.entities.Album;
import com.khramykh.platform.domain.entities.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlbumsRepository extends PagingAndSortingRepository<Album, Integer> {
    Page<Album> findAlbumByNameContaining(String name, Pageable page);
    @Query("select a from Album a where ?1 member a.artists")
    List<Album> findAlbumsByArtistsContaining(Artist artist, Sort sort);
}
