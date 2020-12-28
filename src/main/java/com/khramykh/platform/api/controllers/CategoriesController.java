package com.khramykh.platform.api.controllers;

import com.khramykh.platform.application.categoriesApi.CategoriesService;
import com.khramykh.platform.application.categoriesApi.commands.CategoryCreateCommand;
import com.khramykh.platform.application.categoriesApi.commands.CategoryUpdateCommand;
import com.khramykh.platform.application.commons.sort.CategorySort;
import com.khramykh.platform.domain.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/categories")
public class CategoriesController {
    @Autowired
    CategoriesService categoriesService;

    @GetMapping
    public ResponseEntity getAll(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(required = false) String filter,
            @RequestParam CategorySort categorySort) {
        Page<Category> categoryPage;
        if(filter != null) {
            categoryPage = categoriesService.getCategoriesByNameAndPage(pageNum, pageSize, filter, categorySort);
        } else {
            categoryPage = categoriesService.getCategoriesByPage(pageNum, pageSize, categorySort);
        }
        return ResponseEntity.ok().body(categoryPage);
    }

    @GetMapping("/all")
    public ResponseEntity getAll(@RequestParam(required = false) String categoryName) {
        List<Category> categories;
        if (categoryName != null) {
            categories = categoriesService.getCategoriesByName(categoryName);
        } else {
            categories = categoriesService.getCategories();
        }

        return ResponseEntity.ok().body(categories);
    }

    @GetMapping("{id}")
    public ResponseEntity getOneById(@PathVariable int id) {
        Category categoryPage = categoriesService.getCategoryById(id);
        return ResponseEntity.ok().body(categoryPage);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity delete(@PathVariable int id) {
        categoriesService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity update(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CategoryUpdateCommand command) {
        Category updated = categoriesService.update(command, userDetails.getUsername());
        return ResponseEntity.ok().body(updated);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity create(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CategoryCreateCommand command) {
        Category created = categoriesService.create(command, userDetails.getUsername());
        return ResponseEntity.ok().body(created);
    }
}
