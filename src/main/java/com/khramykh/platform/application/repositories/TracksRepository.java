package com.khramykh.platform.application.repositories;

import com.khramykh.platform.domain.entities.Artist;
import com.khramykh.platform.domain.entities.Track;
import com.khramykh.platform.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TracksRepository extends PagingAndSortingRepository<Track, Integer> {
    Page<Track> findByPublished(boolean published, Pageable pageable);

    Page<Track> findByNameContaining(String name, Pageable pageable);
    Page<Track> findAllByOrderByCreatedDateDesc(Pageable pageable);
    Page<Track> findAllByLikesContains(User user, Pageable pageable);
    Page<Track> findAllByArtistsContains(Artist artist, Pageable pageable);

    List<Track> findByAlbum_Id(int id);
}
