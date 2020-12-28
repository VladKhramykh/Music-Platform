package com.khramykh.platform.application.repositories;

import com.khramykh.platform.domain.entities.Album;
import com.khramykh.platform.domain.entities.Artist;
import com.khramykh.platform.domain.entities.Track;
import com.khramykh.platform.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TracksRepository extends PagingAndSortingRepository<Track, Integer> {
    Page<Track> findByNameContaining(String name, Pageable pageable);

    Page<Track> findByOrderByReleaseDateDesc(Pageable pageable);

    Page<Track> findAllByLikesContains(User user, Pageable pageable);

    Page<Track> findAllByArtistsContains(Artist artist, Pageable pageable);

    @Transactional
    void deleteAllByArtistsContains(Artist artist);

    @Transactional
    void deleteAllByAlbum(Album album);

    List<Track> findByAlbum_Id(int id);
}
