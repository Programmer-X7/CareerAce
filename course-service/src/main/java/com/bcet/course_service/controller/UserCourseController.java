package com.bcet.course_service.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bcet.course_service.dto.APIResponse;
import com.bcet.course_service.service.UserCourseService;

@RestController
@RequestMapping("/courses")
public class UserCourseController {

    private final UserCourseService userCourseService;
    private static final Logger logger = LoggerFactory.getLogger(UserCourseController.class);

    public UserCourseController(UserCourseService userCourseService) {
        this.userCourseService = userCourseService;
    }

    @PostMapping("/enroll")
    public ResponseEntity<APIResponse> enrollCourse(@RequestParam(name = "uid") String userId,
            @RequestParam(name = "cid") UUID courseId) {
        try {
            userCourseService.createUserCourse(userId, courseId);
            return new ResponseEntity<>(new APIResponse("Enroll successful"), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(new APIResponse("Course enrollment failed"));
        }
    }

    @DeleteMapping("/expel")
    public ResponseEntity<APIResponse> expelCourse(@RequestParam(name = "uid") String userId,
            @RequestParam(name = "cid") UUID courseId) {
        try {
            userCourseService.deleteUserCourse(userId, courseId);
            return new ResponseEntity<>(new APIResponse("Expel successful"), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(new APIResponse("Course expel failed"));
        }
    }
    
}
