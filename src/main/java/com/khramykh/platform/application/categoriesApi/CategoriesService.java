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

import java.util.List;

@Service
public class CategoriesService {
    @Autowired
    CategoriesRepository categoryRepository;
    @Autowired
    UsersRepository usersRepository;

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesByName(String name) {
        return categoryRepository.findByNameContaining(name);
    }

    public Page<Category> getCategoriesByPage(int pageNum, int pageSize, CategorySort categorySort) {
        return categoryRepository.findAll(PageRequest.of(pageNum, pageSize, getSortType(categorySort)));
    }

    public void removeById(int id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        categoryRepository.deleteById(id);
    }

    public Category update(CategoryUpdateCommand command, String lastModifiedBy) {
        Category oldCategory = categoryRepository.findById(command.getId()).orElseThrow(() -> new ResourceNotFoundException((command.getId())));
        oldCategory.setLastModifiedBy(lastModifiedBy);
        Category updated = categoryRepository.save(convertCategoryUpdateCommandToCategory(oldCategory, command));
        return updated;
    }

    public Category create(CategoryCreateCommand command, String createdBy) {
        Category category = new Category();
        category.setName(command.getName());
        category.setDescription(command.getDescription());
        category.setCreatedBy(createdBy);
        category.setLastModifiedBy(createdBy);
        categoryRepository.save(category);
        return category;
    }

    private Sort getSortType(CategorySort categorySort) {
        switch (categorySort) {
            case NAME_DESC:
                return Sort.by("name").descending();
            case ID_DESC:
                return Sort.by("id").descending();
            case ID_ASC:
                return Sort.by("id").ascending();
            case DESCRIPTION_DESC:
                return Sort.by("description").descending();
            case DESCRIPTION_ASC:
                return Sort.by("description").ascending();
            default:
                return Sort.by("name").ascending();
        }
    }

    private Category convertCategoryUpdateCommandToCategory(Category oldCategory, CategoryUpdateCommand command) {
        oldCategory.setName(command.getName());
        oldCategory.setDescription(command.getDescription());
        return oldCategory;
    }

    public Page<Category> getCategoriesByNameAndPage(int pageNum, int pageSize, String filter, CategorySort categorySort) {
        return categoryRepository.findByNameContaining(filter, PageRequest.of(pageNum, pageSize, getSortType(categorySort)));
    }
}
