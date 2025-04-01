package com.bcet.course_service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bcet.course_service.dto.AllCoursesDTO;
import com.bcet.course_service.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    // Get all courses for displaying on All Courses Page
    @Query("SELECT new com.bcet.course_service.dto.AllCoursesDTO(c.courseId, c.title, c.slug, c.thumbnail, c.rating, c.originalPrice, c.discount) FROM Course c")
    List<AllCoursesDTO> findAllCourses();

    // Get all details of the courses excluding non-public videos and articles
    Optional<Course> findBySlug(String slug);

    // Update course rating
    @Modifying
    @Transactional
    @Query("UPDATE Course c SET c.rating = :rating WHERE c.courseId = :courseId")
    void updateRating(@Param("courseId") UUID courseId, @Param("rating") float rating);

}
