package com.khramykh.platform.application.repositories;

import com.khramykh.platform.domain.entities.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface ArtistsRepository extends PagingAndSortingRepository<Artist, Integer> {
    Page<Artist> findAllByNameContaining(String name, Pageable pageable, Sort sort);
}
