package com.bcet.mock_test_service.dto;

import java.time.LocalDate;

public record AbstractCategoryDTO(
        String id,
        String slug,
        String name,
        String description,
        long popularity,
        LocalDate createdAt,
        LocalDate updatedAt) {
}
