package com.bcet.resume_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bcet.resume_service.dto.ResumeCardDto;
import com.bcet.resume_service.exceptions.NotAllowedException;
import com.bcet.resume_service.model.Resume;
import com.bcet.resume_service.repository.ResumeRepository;

import jakarta.ws.rs.NotFoundException;

@Service
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;

    public ResumeServiceImpl(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    @Override
    public void createResume(Resume resume) {
        try {
            resumeRepository.save(resume);
        } catch (Exception e) {
            throw new RuntimeException("Error creating resume: " + e.getMessage());
        }
    }

    @Override
    public List<ResumeCardDto> getAllResumesByUserId(String userId) {
        Optional<List<Resume>> optionalResumes = resumeRepository.findByUserId(userId);
        if (!optionalResumes.isPresent()) {
            return new ArrayList<>();
        }

        return optionalResumes.get().stream()
        .map(resume -> new ResumeCardDto(
            resume.getResumeId(),
            resume.getTemplateId(),
            resume.getTitle(),
            resume.getThemeColor()
        )).toList();
    }

    @Override
    public Resume getResumeById(String resumeId) {
        try {
            return resumeRepository.findById(resumeId)
                    .orElseThrow(() -> new NotFoundException("No Resume found with id: " + resumeId));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching resume: " + e.getMessage());
        }
    }

    // @Override
    // public void updateResume(Resume resume) {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'updateResume'");
    // }

    @Override
    public void deleteResume(String resumeId, String userId) {
        try {
            // Find is resume exists
            Optional<Resume> existingResume = resumeRepository.findById(resumeId);
            if (!existingResume.isPresent()) {
                throw new NotFoundException("No Resume found with id: " + resumeId);
            }

            // Check if the user trying do delete is the owner of the resume
            if (!existingResume.get().getUserId().equals(userId)) {
                throw new NotAllowedException("You are not authorized to delete this resume");
            }

            // Delete resume
            resumeRepository.deleteById(resumeId);

        } catch (Exception e) {
            throw new RuntimeException("Error deleting resume: " + e.getMessage());
        }
    }

}
