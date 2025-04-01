package com.bcet.mock_test_service.service;

import com.bcet.mock_test_service.dto.CreateQuestionDTO;
import com.bcet.mock_test_service.model.Category;
import com.bcet.mock_test_service.model.Option;
import com.bcet.mock_test_service.model.Question;
import com.bcet.mock_test_service.model.TestSet;
import com.bcet.mock_test_service.repository.CategoryRepository;
import com.bcet.mock_test_service.repository.OptionRepository;
import com.bcet.mock_test_service.repository.QuestionRepository;
import com.bcet.mock_test_service.repository.TestSetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final CategoryRepository categoryRepository;
    private final TestSetRepository testSetRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    private final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    public QuestionServiceImpl(CategoryRepository categoryRepository, TestSetRepository testSetRepository, QuestionRepository questionRepository, OptionRepository optionRepository) {
        this.categoryRepository = categoryRepository;
        this.testSetRepository = testSetRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
    }

    @Transactional
    @Override
    public void createQuestions(String categorySlug, String testSetSlug, List<CreateQuestionDTO> createQuestionDto) {

        // Find the Category
        Category category = categoryRepository.findBySlug(categorySlug)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Find the TestSet within the Category
        TestSet testSet = testSetRepository.findBySlugAndCategory(testSetSlug, category)
                .orElseThrow(() -> new RuntimeException("TestSet not found in the specified category"));

        // Convert DTOs to Entities
        List<Question> questions = createQuestionDto.stream().map(dto -> {
            Question question = new Question();
            question.setQuestionText(dto.getQuestionText());
            question.setExplanation(dto.getExplanation());
            question.setTestSet(testSet);

            return questionRepository.save(question);
        }).toList();

        // Convert and Save Options
        List<Option> options = createQuestionDto.stream()
                .flatMap(dto -> dto.getOptions().stream()
                        .map(optDto -> {
                            Option option = new Option();
                            option.setOptionText(optDto.getOptionText());
                            option.setOptionOrder(optDto.getOptionOrder());
                            option.setCorrect(optDto.isCorrect());

                            // Find the related question and link
                            Question relatedQuestion = questions.get(createQuestionDto.indexOf(dto));
                            option.setQuestion(relatedQuestion);

                            return option;
                        }))
                .collect(Collectors.toList());

        optionRepository.saveAll(options);
    }

    @Override
    public long getTotalQuestionCount() {
        return questionRepository.count();
    }

    @Transactional
    @Override
    public void deleteQuestion(String questionId) {
        try {
            // Delete options first
            optionRepository.deleteOptionsByQuestionId(questionId);

            // Now Delete the question
            questionRepository.deleteQuestionById(questionId);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
