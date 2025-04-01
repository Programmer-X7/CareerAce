package com.bcet.mock_test_service.controller;

import com.bcet.mock_test_service.dto.CreateTestSetDTO;
import com.bcet.mock_test_service.dto.TestSetDTO;
import com.bcet.mock_test_service.model.TestSet;
import com.bcet.mock_test_service.service.TestSetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test-sets")
public class TestSetController {

    private final TestSetService testSetService;

    public TestSetController(TestSetService testSetService) {
        this.testSetService = testSetService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTestSet(@RequestBody CreateTestSetDTO dto) {
        try {
            testSetService.createTestSet(dto);
            return new ResponseEntity<>("Successfully created Test Set", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating test set: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllTestSetsByCategory(@RequestParam(name = "category") String categorySlug) {
        try {
            List<TestSetDTO> testSets = testSetService.getAllTestSetsByCategory(categorySlug);
            return ResponseEntity.ok(testSets);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error fetching test sets: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/questions")
    public ResponseEntity<?> getSingleTestSetWithQuestions(@RequestParam(name = "category") String categorySlug,
                                                           @RequestParam(name = "set") String testSetSlug) {
        try {
            TestSet testSet = testSetService.getCategoryWithQuestionsAndOptions(categorySlug, testSetSlug);
            return ResponseEntity.ok(testSet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error fetching Questions: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @PutMapping("/update/attempts")
    public ResponseEntity<?> updateAttempts(@RequestParam(name = "sid") String testSetId) {
        try {
            testSetService.increaseAttempts(testSetId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error increment test set attempt: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @PutMapping("/update/duration")
    public ResponseEntity<?> updateDuration(@RequestParam(name = "sid") String testSetId, @RequestParam(name = "duration") int duration) {
        try {
            testSetService.updateDuration(testSetId, duration);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating duration: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTestSet(@RequestParam(name = "category") String categorySlug, @RequestParam(name = "set") String testSetSlug) {
        try {
            testSetService.deleteTestSet(testSetSlug, categorySlug);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting test set: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

}
