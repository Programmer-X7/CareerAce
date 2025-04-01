package com.bcet.mock_test_service.service;

import com.bcet.mock_test_service.dto.CreateTestSetDTO;
import com.bcet.mock_test_service.dto.TestSetDTO;
import com.bcet.mock_test_service.model.Category;
import com.bcet.mock_test_service.model.TestSet;
import com.bcet.mock_test_service.repository.CategoryRepository;
import com.bcet.mock_test_service.repository.TestSetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestSetServiceImpl implements TestSetService {

    private final CategoryRepository categoryRepository;
    private final TestSetRepository testSetRepository;

    public TestSetServiceImpl(CategoryRepository categoryRepository, TestSetRepository testSetRepository) {
        this.categoryRepository = categoryRepository;
        this.testSetRepository = testSetRepository;
    }

    @Override
    public void createTestSet(CreateTestSetDTO dto) {
        // Find Category by Slug
        Category category = categoryRepository.findBySlug(dto.categorySlug())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Create TestSet
        TestSet testSet = new TestSet();
        testSet.setSlug(dto.testSetSlug());
        testSet.setName(dto.testSetName());
        testSet.setDuration(dto.duration());
        testSet.setCategory(category);

        testSetRepository.save(testSet);
    }

    @Override
    public void increaseAttempts(String testSetId) {
        testSetRepository.incrementAttempts(testSetId);
    }

    @Override
    public List<TestSetDTO> getAllTestSetsByCategory(String categorySlug) {
        return testSetRepository.findTestSetsByCategorySlug(categorySlug);
    }

    @Override
    public TestSet getCategoryWithQuestionsAndOptions(String categorySlug, String testSetSlug) {
        List<TestSetDTO> allTestSets = testSetRepository.findTestSetsByCategorySlug(categorySlug);

        if (allTestSets.isEmpty()) {
            throw new RuntimeException("Category not found: " + categorySlug);
        }

        // Filter the needed Abstract TestSet
        TestSetDTO testSetDTO = allTestSets.stream().filter(testSet -> testSet.slug().equals(testSetSlug))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("TestSet not found: " + testSetSlug));

        // Fetch the full TestSet Entity
        return testSetRepository.findById(testSetDTO.testSetId()).orElseThrow(() -> new RuntimeException("Error fetching test set details"));

    }

    @Override
    public void updateDuration(String testSetId, int duration) {
        testSetRepository.updateDuration(testSetId, duration);
    }

    @Override
    public void deleteTestSet(String testSetSlug, String categorySlug) {
        // Find Category by Slug
        Category category = categoryRepository.findBySlug(categorySlug).orElseThrow(() -> new RuntimeException("Category not found"));

        // Find Test Set by Slug and Category
        TestSet testSet = testSetRepository.findBySlugAndCategory(testSetSlug, category).orElseThrow(() -> new RuntimeException("TestSet not found"));

        // Delete Test set
        testSetRepository.delete(testSet);
    }
}
