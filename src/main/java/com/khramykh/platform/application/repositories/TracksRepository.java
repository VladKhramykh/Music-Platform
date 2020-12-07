package com.khramykh.platform.application.repositories;

import com.khramykh.platform.domain.entities.Artist;
import com.khramykh.platform.domain.entities.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TracksRepository extends PagingAndSortingRepository<Track, Integer> {
    Page<Track> findByPublished(boolean published, Pageable pageable);

    Page<Track> findByNameContaining(String name, Pageable pageable);

    @Query("select t from Track t where ?1 member of t.artists")
    Page<Track> findByArtists(Artist artist, Pageable pageable);

    List<Track> findByAlbum_Id(int id);
}
