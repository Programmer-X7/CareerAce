package com.bcet.roadmap_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcet.roadmap_service.dto.ApiResponse;
import com.bcet.roadmap_service.model.Category;
import com.bcet.roadmap_service.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Add a category
    @PostMapping
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
        try {
            categoryService.addCategory(category);
            return new ResponseEntity<>(new ApiResponse("Category successfully added"), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Error adding category!"));
        }
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Error fetching categories"));
        }
    }

    // Update category name
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable String id, @RequestBody Category category) {
        try {
            categoryService.updateCategory(id, category);
            return ResponseEntity.ok(new ApiResponse("Category successfully updated."));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Cannot update category information right now, try again later."));
        }
    }

    // Note: Categories cannot be deleted
}
