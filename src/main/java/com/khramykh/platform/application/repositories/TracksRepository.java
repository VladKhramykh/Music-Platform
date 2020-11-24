package com.khramykh.platform.application.repositories;

import com.khramykh.platform.domain.entities.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface TracksRepository extends PagingAndSortingRepository<Track, Integer> {
    Page<Track> findByPublished(boolean published, Pageable pageable);

    Page<Track> findByNameContaining(String name, Pageable pageable);
}
