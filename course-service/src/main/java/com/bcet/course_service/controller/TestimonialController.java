package com.bcet.course_service.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bcet.course_service.dto.APIResponse;
import com.bcet.course_service.model.Testimonial;
import com.bcet.course_service.service.TestimonialService;

@RestController
@RequestMapping("/courses/reviews")
public class TestimonialController {

    private final TestimonialService testimonialService;

    public TestimonialController(TestimonialService testimonialService) {
        this.testimonialService = testimonialService;
    }

    @PostMapping("/create")
    public ResponseEntity<APIResponse> addTestimonial(@RequestBody Testimonial testimonial,
            @RequestParam(name = "cid") UUID courseId) {
        try {
            testimonialService.addTestimonial(testimonial, courseId);
            return ResponseEntity.ok(new APIResponse("Testimonial added successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new APIResponse("Error adding testimonial"));
        }
    }

    @GetMapping("/all/{courseId}")
    public ResponseEntity<?> getAllTestimonialsByCourseId(@PathVariable UUID courseId) {
        try {
            return ResponseEntity.ok(testimonialService.getAllTestimonialsByCourseId(courseId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new APIResponse("Error fetching testimonials"));
        }
    }

    @GetMapping
    public ResponseEntity<?> getTestimonialById(@RequestParam(name = "uid") String userId, @RequestParam(name = "cid") UUID courseId) {
        try {
            return ResponseEntity.ok(testimonialService.getTestimonialByUserIdAndCourseId(userId, courseId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new APIResponse("Error fetching testimonial"));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> putMethodName(@RequestParam(name = "cid") UUID courseId,
            @RequestBody Testimonial testimonial) {
        try {
            return ResponseEntity.ok(testimonialService.updateTestimonial(testimonial, courseId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new APIResponse("Error updating testimonial"));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTestimonial(@RequestParam(name = "tid") UUID testimonialId,
            @RequestParam(name = "cid") UUID courseId, @RequestParam String role) {
        try {
            testimonialService.removeTestimonial(testimonialId, courseId, role);
            return ResponseEntity.ok(new APIResponse("Testimonial deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new APIResponse("Error deleting testimonial"));
        }
    }
}
