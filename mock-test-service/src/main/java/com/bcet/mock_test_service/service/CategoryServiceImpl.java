package com.bcet.mock_test_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bcet.mock_test_service.dto.AbstractCategoryDTO;
import com.bcet.mock_test_service.dto.CategoryDTO;
import com.bcet.mock_test_service.model.Category;
import com.bcet.mock_test_service.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void increasePopularity(String categoryId) {
        categoryRepository.incrementPopularity(categoryId);
    }

    @Override
    public void updateCategory(String categoryId, String slug, String name, String description) {
        categoryRepository.updateCategory(categoryId, slug, name, description);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAllCategories();
    }

    @Override
    public void deleteCategory(String categorySlug) {
        AbstractCategoryDTO existingCategory = categoryRepository.findCategoryBySlug(categorySlug).orElseThrow(() -> new RuntimeException("Category not found"));

        categoryRepository.deleteById(existingCategory.id());
    }
}
