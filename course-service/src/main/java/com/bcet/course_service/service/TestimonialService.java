package com.bcet.course_service.service;

import java.util.List;
import java.util.UUID;

import com.bcet.course_service.model.Testimonial;

public interface TestimonialService {

    void addTestimonial(Testimonial testimonial, UUID courseId);

    List<Testimonial> getAllTestimonialsByCourseId(UUID courseId);

    Testimonial getTestimonialByUserIdAndCourseId(String userId, UUID courseId);

    Testimonial updateTestimonial(Testimonial testimonial, UUID courseId);

    void removeTestimonial(UUID testimonialId, UUID courseId, String role);
}
