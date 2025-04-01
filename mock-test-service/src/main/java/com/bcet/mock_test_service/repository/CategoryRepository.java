package com.bcet.mock_test_service.repository;

import com.bcet.mock_test_service.dto.AbstractCategoryDTO;
import com.bcet.mock_test_service.dto.CategoryDTO;
import com.bcet.mock_test_service.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query("SELECT new com.bcet.mock_test_service.dto.CategoryDTO( " +
            "c.categoryId, c.slug, c.name, c.description, SIZE(c.testSets), c.popularity, c.createdAt, c.updatedAt) " +
            "FROM Category c")
    List<CategoryDTO> findAllCategories();

    // Find By Slug with abstract fields
    @Query("SELECT new com.bcet.mock_test_service.dto.AbstractCategoryDTO( " +
            "c.categoryId, c.slug, c.name, c.description, c.popularity, c.createdAt, c.updatedAt) " +
            "FROM Category c WHERE c.slug = :slug")
    Optional<AbstractCategoryDTO> findCategoryBySlug(String slug);

    // Find all details of the category
    Optional<Category> findBySlug(String slug);

    // Increase popularity by 1 for every attempt for category
    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.popularity = c.popularity + 1 WHERE c.categoryId = :categoryId")
    void incrementPopularity(@Param("categoryId") String categoryId);

    // Update Category name
    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.name = :name, c.slug = :slug, c.description = :description WHERE c.categoryId = :categoryId")
    void updateCategory(@Param("categoryId") String categoryId, @Param("slug") String slug, @Param("name") String name, @Param("description") String description);

}
