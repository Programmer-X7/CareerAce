package com.bcet.mock_test_service.controller;

import com.bcet.mock_test_service.dto.CategoryDTO;
import com.bcet.mock_test_service.dto.CategoryUpdateRequestDTO;
import com.bcet.mock_test_service.model.Category;
import com.bcet.mock_test_service.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        try {
            categoryService.createCategory(category);
            return new ResponseEntity<>("Successfully created category", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating mock test category: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        try {
            List<CategoryDTO> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error fetching mock test categories: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @PutMapping("/update/popularity")
    public ResponseEntity<?> updatePopularity(@RequestParam(name = "cid") String categoryId) {
        try {
            categoryService.increasePopularity(categoryId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error increment popularity: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> renameCategory(@RequestParam(name = "cid") String categoryId,
            @RequestBody CategoryUpdateRequestDTO dto) {
        try {
            categoryService.updateCategory(categoryId, dto.categorySlug(), dto.categoryName(),
                    dto.categoryDescription());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error rename category: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCategory(@RequestParam(name = "category") String categorySlug) {
        try {
            categoryService.deleteCategory(categorySlug);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error deleting mock test category: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }
}
