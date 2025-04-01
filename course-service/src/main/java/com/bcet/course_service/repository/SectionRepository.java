package com.bcet.course_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcet.course_service.model.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, UUID> {
    
}
