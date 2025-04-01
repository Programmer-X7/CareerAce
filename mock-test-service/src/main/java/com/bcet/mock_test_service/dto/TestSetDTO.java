package com.bcet.mock_test_service.dto;

import jakarta.persistence.Column;

import java.time.LocalDate;

public record TestSetDTO(
        String testSetId,
        String slug,
        String name,
        int questionCount,
        int duration,
        long attempts,
        LocalDate createdAt,
        LocalDate updatedAt
) {}
