package com.bcet.resume_service.service;

import java.util.List;

import com.bcet.resume_service.dto.ResumeCardDto;
import com.bcet.resume_service.model.Resume;

public interface ResumeService {

    void createResume(Resume resume);

    List<ResumeCardDto> getAllResumesByUserId(String userId);

    Resume getResumeById(String resumeId);

    // void updateResume(Resume resume);

    void deleteResume(String resumeId, String userId);
}