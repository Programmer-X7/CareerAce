package com.bcet.course_service.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcet.course_service.model.UserCourse;
import com.bcet.course_service.repository.UserCourseRepository;

@Service
public class UserCourseServiceImpl implements UserCourseService {

    private final UserCourseRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(UserCourseServiceImpl.class);

    public UserCourseServiceImpl(UserCourseRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createUserCourse(String userId, UUID courseId) {
        try {
            UserCourse userCourse = new UserCourse();
            userCourse.setUserId(userId);
            userCourse.setCourseId(courseId);
            repository.save(userCourse);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error creating user-course: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteUserCourse(String userId, UUID courseId) {
        try {
            if (repository.existsByUserIdAndCourseId(userId, courseId)) {
                repository.deleteByUserIdAndCourseId(userId, courseId);
            } else {
                throw new RuntimeException("User-course not found");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error deleting user-course: " + e.getMessage());
        }
    }

}
