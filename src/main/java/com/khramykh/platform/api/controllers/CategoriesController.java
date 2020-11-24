package com.khramykh.platform.api.controllers;

import com.khramykh.platform.application.categoriesApi.CategoriesService;
import com.khramykh.platform.application.categoriesApi.commands.CategoryCreateCommand;
import com.khramykh.platform.application.categoriesApi.commands.CategoryUpdateCommand;
import com.khramykh.platform.application.commons.sort.CategorySort;
import com.khramykh.platform.domain.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/categories")
public class CategoriesController {
    @Autowired
    CategoriesService categoriesService;

    @GetMapping
    public ResponseEntity getAll(@RequestParam int pageNum, @RequestParam int pageSize, @RequestParam CategorySort categorySort) {
        Page<Category> categoryPage = categoriesService.getCategoriesByPage(pageNum, pageSize, categorySort);
        return ResponseEntity.ok().body(categoryPage);
    }

    @GetMapping("{id}")
    public ResponseEntity getOneById(@PathVariable int id) {
        Category categoryPage = categoriesService.getCategoryById(id);
        return ResponseEntity.ok().body(categoryPage);
    }

    @GetMapping("/like")
    public ResponseEntity like(@RequestParam int trackId, @RequestParam int userId) {
        categoriesService.like(trackId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dislike")
    public ResponseEntity dislike(@RequestParam int trackId, @RequestParam int userId) {
        categoriesService.dislike(trackId, userId);
        return ResponseEntity.ok().build();
    }

//    @GetMapping
//    public ResponseEntity getOneByName(@RequestParam String name, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam CategorySort categorySort) {
//        Page categoryPage = categoriesService.getCategoryByName(name, pageNum, pageSize, categorySort);
//        return ResponseEntity.ok().body(categoryPage);
//    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable int id) {
        categoriesService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity update(@RequestBody CategoryUpdateCommand command) {
        Category updated = categoriesService.update(command);
        return ResponseEntity.ok().body(updated);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody CategoryCreateCommand command) {
        Category created = categoriesService.create(command);
        return ResponseEntity.ok().body(created);
    }
}
