package com.bcet.resume_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcet.resume_service.model.Resume;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, String> {

    Optional<List<Resume>> findByUserId(String userId);
}
