package com.khramykh.platform.application.repositories;

import com.khramykh.platform.domain.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
    Page<Category> findAllByNameContaining(String name, Pageable pageable, Sort sort);
}
