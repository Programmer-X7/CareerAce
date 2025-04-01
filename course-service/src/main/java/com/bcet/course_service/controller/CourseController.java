package com.bcet.course_service.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcet.course_service.dto.APIResponse;
import com.bcet.course_service.dto.AllCoursesDTO;
import com.bcet.course_service.model.Course;
import com.bcet.course_service.model.Section;
import com.bcet.course_service.service.CourseService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/create")
    public ResponseEntity<APIResponse> createCourse(@RequestBody Course course) {
        try {
            logger.info("Create course: " + course);
            courseService.createCourse(course);
            return new ResponseEntity<>(new APIResponse("Course created successfully"), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new APIResponse("Error creating course"));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        try {
            List<AllCoursesDTO> allCourses = courseService.getAllCourses();
            return ResponseEntity.ok(allCourses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new APIResponse("Error fetching courses"));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserCourses(@RequestParam(name = "uid") String userId) {
        try {
            List<AllCoursesDTO> userCourses = courseService.getUserCourses(userId);
            return ResponseEntity.ok(userCourses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new APIResponse("Error fetching courses"));
        }
    }

    @GetMapping("/{slug}")
    public ResponseEntity<?> getCourseDetails(@PathVariable String slug) {
        try {
            Course courseDetails = courseService.getCourseDetails(slug);
            return ResponseEntity.ok(courseDetails);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new APIResponse("Error fetching course details"));
        }
    }

    @GetMapping("/take/{slug}")
    public ResponseEntity<?> consumeCourse(@PathVariable String slug, @RequestParam(name = "uid") String userId,
            @RequestParam String role) {
        try {
            Course fullCourse = courseService.consumeCourse(slug, userId, role);
            return ResponseEntity.ok(fullCourse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new APIResponse("Error taking course"));
        }
    }

    @PutMapping("/update/details")
    public ResponseEntity<?> updateCourseDetails(@RequestBody Course course) {
        try {
            Course updatedCourse = courseService.updateCourseDetails(course);
            return ResponseEntity.ok(updatedCourse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new APIResponse("Error updating course details"));
        }
    }

    @PutMapping("/update/modules/{courseId}")
    public ResponseEntity<?> updateCourseModules(@PathVariable UUID courseId, @RequestBody List<Section> sections) {
        try {
            Course updatedCourse = courseService.updateCourseModules(courseId, sections);
            return ResponseEntity.ok(updatedCourse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new APIResponse("Error updating course modules"));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<APIResponse> deleteCourse(@RequestParam(name = "cid") UUID courseId) {
        try {
            courseService.deleteCourse(courseId);
            return new ResponseEntity<>(new APIResponse("Course deleted successfully"), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new APIResponse("Error deleting course"));
        }
    }

}
