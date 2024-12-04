package com.bcet.roadmap_service.service;

import java.util.List;

import com.bcet.roadmap_service.model.Category;

public interface CategoryService {

    void addCategory(Category category);

    List<Category> getAllCategories();

    void updateCategory(String id, Category category);

}
