package com.bcet.course_service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bcet.course_service.model.Testimonial;

public interface TestimonialRepository extends JpaRepository<Testimonial, UUID> {

    @Query("SELECT t FROM Testimonial t WHERE t.course.id = :courseId")
    List<Testimonial> findByCourseId(@Param("courseId") UUID courseId);

    @Query("SELECT t FROM Testimonial t WHERE t.userId = :userId AND t.course.id = :courseId")
    Optional<Testimonial> findByUserIdAndCourseId(@Param("userId") String userId, @Param("courseId") UUID courseId);

}
