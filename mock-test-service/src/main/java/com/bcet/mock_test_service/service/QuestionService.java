package com.bcet.mock_test_service.service;

import com.bcet.mock_test_service.dto.CreateQuestionDTO;

import java.util.List;

public interface QuestionService {

    void createQuestions(String categorySlug, String testSetSlug, List<CreateQuestionDTO> createQuestionDto);

    void deleteQuestion(String questionId);

    long getTotalQuestionCount();
}
