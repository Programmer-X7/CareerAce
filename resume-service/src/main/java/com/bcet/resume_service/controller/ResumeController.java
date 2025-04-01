package com.bcet.resume_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bcet.resume_service.dto.ApiResponse;
import com.bcet.resume_service.dto.ResumeCardDto;
import com.bcet.resume_service.exceptions.NotAllowedException;
import com.bcet.resume_service.model.Resume;
import com.bcet.resume_service.service.ResumeService;

import jakarta.ws.rs.NotFoundException;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/builder")
public class ResumeController {

    private final ResumeService resumeService;
    private static final Logger logger = LoggerFactory.getLogger(ResumeController.class);

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resume/create")
    public ResponseEntity<ApiResponse> createResume(@RequestBody Resume resume) {
        try {
            resumeService.createResume(resume);
            return new ResponseEntity<>(new ApiResponse("Resume created successfully"), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Error creating resume"));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllResumesByUserId(@PathVariable String userId) {
        try {
            List<ResumeCardDto> userResumes = resumeService.getAllResumesByUserId(userId);
            return ResponseEntity.ok(userResumes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Error fetching resumes"));
        }
    }

    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<?> getResumeDetails(@PathVariable String resumeId) {
        try {
            Resume resumeDetails = resumeService.getResumeById(resumeId);
            return ResponseEntity.ok(resumeDetails);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ApiResponse("Resume not found"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Error fetching resume"));
        }
    }

    @PutMapping("resume/{resumeId}/update")
    public ResponseEntity<?> updateResumeInfo(@PathVariable String resumeId, @RequestBody Resume updatedResume) {
        try {
            logger.info("Updating resume data: {}", updatedResume);
            Resume updated = resumeService.updateResume(resumeId, updatedResume);
            return ResponseEntity.ok(updated);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Controller Resume Update Exception: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(new ApiResponse("Internal Server Error"));
        }
    }

    @DeleteMapping("/resume/{resumeId}")
    public ResponseEntity<ApiResponse> deleteResume(@PathVariable String resumeId, @RequestParam String userId) {
        try {
            resumeService.deleteResume(resumeId, userId);
            return new ResponseEntity<>(new ApiResponse("Resume deleted successfully"), HttpStatus.NO_CONTENT);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ApiResponse("Resume not found"), HttpStatus.NOT_FOUND);
        } catch (NotAllowedException e) {
            return new ResponseEntity<>(new ApiResponse("You are not allowed to perform this operation"),
                    HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Resume Deletion failed"));
        }
    }
}
