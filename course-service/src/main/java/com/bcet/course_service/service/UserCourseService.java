package com.bcet.course_service.service;

import java.util.UUID;

public interface UserCourseService {

    void createUserCourse(String userId, UUID courseId);

    void deleteUserCourse(String userId, UUID courseId);

}
