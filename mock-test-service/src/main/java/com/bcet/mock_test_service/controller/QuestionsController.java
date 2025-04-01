package com.bcet.mock_test_service.controller;

import com.bcet.mock_test_service.dto.CreateQuestionDTO;
import com.bcet.mock_test_service.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionsController {

    private final QuestionService questionService;

    public QuestionsController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createQuestions(
            @RequestParam(name = "category") String categorySlug,
            @RequestParam(name = "set") String testSetSlug,
            @RequestBody List<CreateQuestionDTO> createQuestionDTOs) {

//        logger.info("1. Create QuestionDTOs: {}", createQuestionDTOs);

        try {
            questionService.createQuestions(categorySlug, testSetSlug, createQuestionDTOs);
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created Questions");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating Questions: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    // Get total question count across all the categories
    @GetMapping("total-count")
    public ResponseEntity<?> getTotalQuestionCount() {
        try {
            long totalQuestionCount = questionService.getTotalQuestionCount();
            return ResponseEntity.ok(totalQuestionCount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteQuestion(@RequestParam(name = "qid") String questionId) {
        try {
            questionService.deleteQuestion(questionId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting Question: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

}