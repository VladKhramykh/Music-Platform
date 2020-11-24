package com.khramykh.platform.application.categoriesApi;

import com.khramykh.platform.application.categoriesApi.commands.CategoryCreateCommand;
import com.khramykh.platform.application.categoriesApi.commands.CategoryUpdateCommand;
import com.khramykh.platform.application.commons.sort.CategorySort;
import com.khramykh.platform.application.exceptions.ResourceNotFoundException;
import com.khramykh.platform.application.repositories.CategoriesRepository;
import com.khramykh.platform.application.repositories.UsersRepository;
import com.khramykh.platform.domain.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CategoriesService {
    @Autowired
    CategoriesRepository categoryRepository;
    @Autowired
    UsersRepository usersRepository;

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Page<Category> getCategoryByName(String name, int pageNum, int pageSize, CategorySort categorySort) {
        return categoryRepository.findByNameContaining(name, PageRequest.of(pageNum, pageSize, getSortType(categorySort)));
    }

    public Page<Category> getCategoriesByPage(int pageNum, int pageSize, CategorySort categorySort) {
        return categoryRepository.findAll(PageRequest.of(pageNum, pageSize, getSortType(categorySort)));
    }

    // TODO need to realize finding top-10 categories
    public Page<Category> getMostPopularCategoriesByPage(int pageNum, int pageSize, CategorySort categorySort) {
        return categoryRepository.findAll(PageRequest.of(pageNum, pageSize, getSortType(categorySort)));
    }

    public void removeById(int id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        categoryRepository.deleteById(id);
    }

    public Category update(CategoryUpdateCommand command) {
        Category oldCategory = categoryRepository.findById(command.getId()).orElseThrow(() -> new ResourceNotFoundException((command.getId())));
        Category updated = categoryRepository.save(convertCategoryUpdateCommandToCategory(oldCategory, command));
        return updated;
    }

    public Category create(CategoryCreateCommand command) {
        Category category = new Category();
        category.setName(command.getName());
        category.setDescription(command.getDescription());
        categoryRepository.save(category);
        return category;
    }

    public void like(int categoryId, int userId) {
        categoryRepository.findById(categoryId).map(category -> {
            usersRepository.findById(userId).map(user -> category.getLikes().add(user));
            return categoryRepository.save(category);
        });
    }

    public void dislike(int categoryId, int userId) {
        categoryRepository.findById(categoryId).map(category -> {
            usersRepository.findById(userId).map(user -> category.getLikes().remove(user));
            return categoryRepository.save(category);
        });
    }

    private Sort getSortType(CategorySort categorySort) {
        switch (categorySort) {
            case NAME_DESC:
                return Sort.by("name").descending();
            default:
                return Sort.by("name").ascending();
        }
    }

    private Category convertCategoryUpdateCommandToCategory(Category oldCategory, CategoryUpdateCommand command) {
        oldCategory.setName(command.getName());
        oldCategory.setDescription(command.getDescription());
        return oldCategory;
    }
}
