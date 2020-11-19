package com.khramykh.platform.application.repositories;

import com.khramykh.platform.domain.entities.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagingAndSortingRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID> {
    Page<T> findAll(Pageable pageable);
}
