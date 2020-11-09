package com.khramykh.platform.application.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface PagingAndSortingRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    List<T> findAll(Sort sort);
    Page<T> findAll(Pageable pageable);
    Page<T> findAll(Pageable pageable, Sort sort);
}
