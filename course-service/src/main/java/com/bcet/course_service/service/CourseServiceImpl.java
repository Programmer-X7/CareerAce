package com.bcet.course_service.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcet.course_service.dto.AllCoursesDTO;
import com.bcet.course_service.model.Content;
import com.bcet.course_service.model.Course;
import com.bcet.course_service.model.Section;
import com.bcet.course_service.model.Testimonial;
import com.bcet.course_service.model.UserCourse;
import com.bcet.course_service.repository.ContentRepository;
import com.bcet.course_service.repository.CourseRepository;
import com.bcet.course_service.repository.SectionRepository;
import com.bcet.course_service.repository.UserCourseRepository;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;
    private final ContentRepository contentRepository;
    private final UserCourseRepository userCourseRepository;
    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    public CourseServiceImpl(CourseRepository courseRepository, UserCourseRepository userCourseRepository,
            SectionRepository sectionRepository, ContentRepository contentRepository) {
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
        this.contentRepository = contentRepository;
        this.userCourseRepository = userCourseRepository;
    }

    @Override
    public void createCourse(Course course) {
        try {
            if (course.getSections() != null) {
                for (var section : course.getSections()) {
                    section.setCourse(course);
                    if (section.getContents() != null) {
                        for (var content : section.getContents()) {
                            content.setSection(section);
                        }
                    }
                }
            }

            courseRepository.save(course);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error creating course: " + e.getMessage());
        }
    }

    @Override
    public List<AllCoursesDTO> getAllCourses() {
        return courseRepository.findAllCourses();
    }

    @Override
    public List<AllCoursesDTO> getUserCourses(String userId) {
        List<AllCoursesDTO> allCourses = courseRepository.findAllCourses();
        List<UserCourse> userCourses = userCourseRepository.findByUserId(userId);

        // Extract courseIds that the user has purchased
        Set<UUID> purchasedCourseIds = userCourses.stream()
                .map(UserCourse::getCourseId)
                .collect(Collectors.toSet());

        // Filter the allCourses list to only include courses that the user has
        // purchased
        List<AllCoursesDTO> userPurchasedCourses = allCourses.stream()
                .filter(course -> purchasedCourseIds.contains(course.courseId())) // filter courses based on courseId
                .collect(Collectors.toList());

        return userPurchasedCourses;
    }

    @Override
    public Course getCourseDetails(String slug) {
        Optional<Course> optionalCourse = courseRepository.findBySlug(slug);

        if (!optionalCourse.isPresent()) {
            throw new RuntimeException("Course not found with slug: " + slug);
        }

        Course course = optionalCourse.get();

        // Filter out non-public video URLs from content
        for (Section section : course.getSections()) {
            section.setContents(section.getContents().stream()
                    .map(content -> {
                        if (!content.isVisible()) {
                            content.setVideoURL(null); // Hide non-public video URLs
                            content.setArticleURL(null); // Hide non-public Article URLs
                        }
                        return content;
                    })
                    .toList());
        }

        // Filter latest testimonials (limit max 6)
        List<Testimonial> latestTestimonials = course.getTestimonials().stream()
                .filter(t -> t.getReview() != null && !t.getReview().isBlank()) // Remove null/blank reviews
                .sorted(Comparator.comparing(Testimonial::getCreatedAt).reversed()) // Sort by createdAt desc
                .limit(6) // Limit to 6 testimonials
                .toList();

        course.setTestimonials(latestTestimonials);

        return course;
    }

    @Override
    public Course consumeCourse(String slug, String userId, String role) {
        Course course = courseRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Course not found with slug: " + slug));

        // Check if user has purchased the course
        if (!role.equalsIgnoreCase("admin")
                && !userCourseRepository.existsByUserIdAndCourseId(userId, course.getCourseId())) {
            logger.info("User ID: " + userId + " has not purchased the course!");
            throw new RuntimeException("Access Denied! User has not purchased the course: " + slug);
        }

        return course;
    }

    @Override
    public Course updateCourseDetails(Course course) {
        try {
            Optional<Course> optionalCourse = courseRepository.findById(course.getCourseId());

            if (!optionalCourse.isPresent()) {
                throw new RuntimeException("Course not found with id: " + course.getCourseId());
            }

            Course existingCourse = optionalCourse.get();

            if (course.getTitle() != null)
                existingCourse.setTitle(course.getTitle());
            if (course.getSlug() != null)
                existingCourse.setSlug(course.getSlug());
            if (course.getThumbnail() != null)
                existingCourse.setThumbnail(course.getThumbnail());
            if (course.getDescription() != null)
                existingCourse.setDescription(course.getDescription());
            if (course.getLanguage() != null)
                existingCourse.setLanguage(course.getLanguage());
            if (course.getOriginalPrice() != 0)
                existingCourse.setOriginalPrice(course.getOriginalPrice());
            if (course.getDiscount() >= 0 && course.getDiscount() <= 100)
                existingCourse.setDiscount(course.getDiscount());
            if (course.getFeatures() != null)
                existingCourse.setFeatures(course.getFeatures());
            if (course.getHighlights() != null)
                existingCourse.setHighlights(course.getHighlights());

            return courseRepository.save(existingCourse);
        } catch (Exception e) {
            logger.error("Error Updating: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Course updateCourseModules(UUID courseId, List<Section> updatedSections) {
        try {
            Course existingCourse = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

            // Convert existing sections to a map for quick lookup
            Map<UUID, Section> existingSectionsMap = existingCourse.getSections().stream()
                    .collect(Collectors.toMap(Section::getSectionId, section -> section));

            List<Section> newSectionsList = new ArrayList<>();

            for (Section updatedSection : updatedSections) {
                if (updatedSection.getSectionId() != null
                        && existingSectionsMap.containsKey(updatedSection.getSectionId())) {
                    // Update existing section
                    Section existingSection = existingSectionsMap.get(updatedSection.getSectionId());
                    existingSection.setTitle(updatedSection.getTitle());
                    existingSection.setSectionOrder(updatedSection.getSectionOrder());

                    updateContents(existingSection, updatedSection.getContents());

                    newSectionsList.add(existingSection);
                    existingSectionsMap.remove(updatedSection.getSectionId()); // Mark as processed
                } else {
                    // Add new section
                    updatedSection.setCourse(existingCourse);
                    newSectionsList.add(updatedSection);
                }
            }

            // Remove sections that were not in the updated list
            for (Section removedSection : existingSectionsMap.values()) {
                existingCourse.getSections().remove(removedSection);
                sectionRepository.delete(removedSection);
            }

            // Instead of replacing the list, update the existing collection
            existingCourse.getSections().clear();
            existingCourse.getSections().addAll(newSectionsList);

            return courseRepository.save(existingCourse);
        } catch (Exception e) {
            logger.error("Error Updating: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // helper for update course modules
    private void updateContents(Section existingSection, List<Content> updatedContents) {
        if (updatedContents == null)
            return;

        Map<UUID, Content> existingContentsMap = existingSection.getContents().stream()
                .collect(Collectors.toMap(Content::getContentId, content -> content));

        List<Content> newContentsList = new ArrayList<>();

        for (Content updatedContent : updatedContents) {
            if (updatedContent.getContentId() != null
                    && existingContentsMap.containsKey(updatedContent.getContentId())) {
                // Update existing content
                Content existingContent = existingContentsMap.get(updatedContent.getContentId());
                existingContent.setTitle(updatedContent.getTitle());
                existingContent.setContentOrder(updatedContent.getContentOrder());
                existingContent.setVideoURL(updatedContent.getVideoURL());
                existingContent.setArticleURL(updatedContent.getArticleURL());
                existingContent.setVisible(updatedContent.isVisible());

                newContentsList.add(existingContent);
                existingContentsMap.remove(updatedContent.getContentId()); // Mark as processed
            } else {
                // Add new content
                updatedContent.setSection(existingSection);
                newContentsList.add(updatedContent);
            }
        }

        // Remove contents that were not in the updated list
        for (Content removedContent : existingContentsMap.values()) {
            existingSection.getContents().remove(removedContent);
            contentRepository.delete(removedContent);
        }

        // Update existing collection instead of replacing it
        existingSection.getContents().clear();
        existingSection.getContents().addAll(newContentsList);
    }

    @Override
    @Transactional
    public void deleteCourse(UUID courseId) {
        try {
            boolean flag = courseRepository.existsById(courseId);

            if (!flag) {
                throw new RuntimeException("Course not found with id: " + courseId);
            }

            // delete entries from User-Course table
            userCourseRepository.deleteByCourseId(courseId);

            // Delete Course
            courseRepository.deleteById(courseId);
        } catch (Exception e) {
            logger.error("Error Deletion: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}