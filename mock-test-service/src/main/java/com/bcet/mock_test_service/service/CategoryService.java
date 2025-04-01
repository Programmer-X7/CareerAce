package com.bcet.mock_test_service.service;

import com.bcet.mock_test_service.dto.CategoryDTO;
import com.bcet.mock_test_service.model.Category;

import java.util.List;

public interface CategoryService {

    void createCategory(Category category);

    List<CategoryDTO> getAllCategories();

    void increasePopularity(String categoryId);

    void updateCategory(String categoryId, String slug, String name, String description);

    void deleteCategory(String categorySlug);

}
