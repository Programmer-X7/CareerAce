package com.bcet.course_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcet.course_service.model.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID>{
    
    boolean existsByUserIdAndContentId(String userId, UUID contentId);
    
    Optional<Note> findByUserIdAndContentId(String userId, UUID contentId);

}
