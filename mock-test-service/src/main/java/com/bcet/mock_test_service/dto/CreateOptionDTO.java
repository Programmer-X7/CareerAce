package com.bcet.mock_test_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateOptionDTO {
    private String optionText;
    private int optionOrder;

    @JsonProperty("isCorrect") // Ensures proper JSON deserialization
    private boolean isCorrect;
}
