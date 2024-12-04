package com.bcet.roadmap_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bcet.roadmap_service.model.Category;
import com.bcet.roadmap_service.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addCategory(Category category) {
        try {
            repository.save(category);
        } catch (Exception e) {
            throw new RuntimeException("Error saving category: " + category);
        }
    }

    @Override
    public List<Category> getAllCategories() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error Fetching All Categories");
        }
    }

    @Override
    public void updateCategory(String id, Category category) {
        try {
            Category existingCategory = repository.findById(UUID.fromString(id)).orElseThrow(
                    () -> new RuntimeException("Category not found with ID: " + id));

            if (!existingCategory.getName().equals(category.getName())) {
                existingCategory.setName(category.getName());
                repository.save(existingCategory);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error Updating Category: " + category);
        }
    }

}
