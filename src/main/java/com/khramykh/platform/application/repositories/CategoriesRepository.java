package com.khramykh.platform.application.repositories;

import com.khramykh.platform.domain.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CategoriesRepository extends PagingAndSortingRepository<Category, Integer> {
    List<Category> findByNameContaining(String name);
}
