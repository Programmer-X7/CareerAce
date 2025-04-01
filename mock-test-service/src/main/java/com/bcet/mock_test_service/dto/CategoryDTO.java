package com.bcet.mock_test_service.dto;

import java.time.LocalDate;

public record CategoryDTO(
        String id,
        String slug,
        String name,
        String description,
        int testSetCount,
        long popularity,
        LocalDate createdAt,
        LocalDate updatedAt) {
}
