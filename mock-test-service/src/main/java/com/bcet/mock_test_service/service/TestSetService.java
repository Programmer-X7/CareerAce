package com.bcet.mock_test_service.service;

import com.bcet.mock_test_service.dto.CreateTestSetDTO;
import com.bcet.mock_test_service.dto.TestSetDTO;
import com.bcet.mock_test_service.model.TestSet;

import java.util.List;

public interface TestSetService {

    void createTestSet(CreateTestSetDTO dto);

    List<TestSetDTO> getAllTestSetsByCategory(String categorySlug);

    TestSet getCategoryWithQuestionsAndOptions(String categorySlug, String testSetSlug);

    void increaseAttempts(String testSetId);

    void updateDuration(String testSetId, int duration);

    void deleteTestSet(String testSetSlug, String categorySlug);

}
