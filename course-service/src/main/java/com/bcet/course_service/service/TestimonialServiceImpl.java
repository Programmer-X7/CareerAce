package com.bcet.course_service.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcet.course_service.model.Course;
import com.bcet.course_service.model.Testimonial;
import com.bcet.course_service.repository.CourseRepository;
import com.bcet.course_service.repository.TestimonialRepository;
import com.bcet.course_service.repository.UserCourseRepository;

@Service
public class TestimonialServiceImpl implements TestimonialService {

    private final TestimonialRepository testimonialRepository;
    private final CourseRepository courseRepository;
    private final UserCourseRepository userCourseRepository;
    private static final Logger logger = LoggerFactory.getLogger(TestimonialServiceImpl.class);

    public TestimonialServiceImpl(TestimonialRepository testimonialRepository, CourseRepository courseRepository,
            UserCourseRepository userCourseRepository) {
        this.testimonialRepository = testimonialRepository;
        this.courseRepository = courseRepository;
        this.userCourseRepository = userCourseRepository;
    }

    @Override
    @Transactional
    public void addTestimonial(Testimonial testimonial, UUID courseId) {
        try {
            // Check if user had purchased the course
            if (!userCourseRepository.existsByUserIdAndCourseId(testimonial.getUserId(), courseId)) {
                throw new RuntimeException("Purchase the course to review");
            }

            Optional<Course> optionalCourse = courseRepository.findById(courseId);
            if (optionalCourse.isPresent()) {
                Course course = optionalCourse.get();

                testimonial.setCourse(course);
                testimonialRepository.save(testimonial);

                // Update the average rating of the course
                List<Testimonial> testimonials = testimonialRepository.findByCourseId(courseId);
                float newRating = 0f;
                for (Testimonial t : testimonials) {
                    newRating += t.getRating();
                }
                newRating /= testimonials.size();

                // Round to one decimal place
                newRating = Math.round(newRating * 10.0f) / 10.0f;

                courseRepository.updateRating(courseId, newRating);
            } else {
                throw new RuntimeException("Course not found with id: " + courseId);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Testimonial> getAllTestimonialsByCourseId(UUID courseId) {
        try {
            return testimonialRepository.findByCourseId(courseId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Course not found with id: " + courseId);
        }
    }

    @Override
    public Testimonial getTestimonialByUserIdAndCourseId(String userId, UUID courseId) {
        try {
            Optional<Testimonial> optionalTestimonial = testimonialRepository.findByUserIdAndCourseId(userId, courseId);
            if (optionalTestimonial.isPresent()) {
                return optionalTestimonial.get();
            } else {
                throw new RuntimeException("Testimonial not found with userId: " + userId + " && courseId: " + courseId);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public Testimonial updateTestimonial(Testimonial testimonial, UUID courseId) {
        try {
            // Check if user had purchased the course
            if (!userCourseRepository.existsByUserIdAndCourseId(testimonial.getUserId(), courseId)) {
                throw new RuntimeException("Purchase the course to review");
            }

            Optional<Testimonial> optionalTestimonial = testimonialRepository.findById(testimonial.getTestimonialId());
            if (optionalTestimonial.isPresent()) {
                Testimonial existingTestimonial = optionalTestimonial.get();
                existingTestimonial.setReview(testimonial.getReview());
                existingTestimonial.setRating(testimonial.getRating());
                Testimonial savedTestimonial = testimonialRepository.save(existingTestimonial);

                // Update the average rating of the course
                List<Testimonial> testimonials = testimonialRepository.findByCourseId(courseId);
                float newRating = 0f;
                for (Testimonial t : testimonials) {
                    newRating += t.getRating();
                }
                newRating /= testimonials.size();

                // Round to one decimal place
                newRating = Math.round(newRating * 10.0f) / 10.0f;

                courseRepository.updateRating(courseId, newRating);

                // return updated testimonial
                return savedTestimonial;
            } else {
                throw new RuntimeException("Testimonial not found with id: " + testimonial.getTestimonialId());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void removeTestimonial(UUID testimonialId, UUID courseId, String role) {
        try {
            Optional<Testimonial> optionalTestimonial = testimonialRepository.findById(testimonialId);

            if (optionalTestimonial.isPresent()) {
                // Check if user had purchased the course
                if (!role.equalsIgnoreCase("admin") && !userCourseRepository.existsByUserIdAndCourseId(optionalTestimonial.get().getUserId(), courseId)) {
                    throw new RuntimeException("Purchase the course to review");
                }
                testimonialRepository.deleteById(testimonialId);

                // Update the average rating of the course
                List<Testimonial> testimonials = testimonialRepository.findByCourseId(courseId);
                float newRating = 0f;
                for (Testimonial t : testimonials) {
                    newRating += t.getRating();
                }
                newRating /= testimonials.size();

                // Round to one decimal place
                newRating = Math.round(newRating * 10.0f) / 10.0f;
                courseRepository.updateRating(courseId, newRating);
            } else {
                throw new RuntimeException("Testimonial not found with id: " + testimonialId);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
