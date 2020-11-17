package com.khramykh.platform.application.repositories;

import com.khramykh.platform.domain.entities.Category;

import java.util.Optional;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
    Optional<Category> findByName(String name);
}
