package com.bcet.course_service.dto;

import java.util.UUID;

public record AllCoursesDTO(UUID courseId, String title, String slug, String thumbnail, float rating, int originalPrice,
        float discount) {

}
