package com.bcet.course_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bcet.course_service.model.UserCourse;

import feign.Param;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, UUID> {

    List<UserCourse> findByUserId(String userId);

    boolean existsByUserIdAndCourseId(String userId, UUID courseId);

    @Modifying
    @Query("DELETE FROM UserCourse uc WHERE uc.userId = :userId AND uc.courseId = :courseId")
    @Transactional
    void deleteByUserIdAndCourseId(@Param("userId") String userId, @Param("courseId") UUID courseId);

    void deleteByCourseId(UUID courseId);

}
