package com.bcet.course_service.service;

import java.util.List;
import java.util.UUID;

import com.bcet.course_service.dto.AllCoursesDTO;
import com.bcet.course_service.model.Course;
import com.bcet.course_service.model.Section;

public interface CourseService {

    void createCourse(Course course);

    List<AllCoursesDTO> getAllCourses();

    List<AllCoursesDTO> getUserCourses(String userId);

    Course getCourseDetails(String slug);

    Course consumeCourse(String slug, String userId, String role);

    Course updateCourseDetails(Course course);

    Course updateCourseModules(UUID courseId, List<Section> sections);

    void deleteCourse(UUID courseId);

}
