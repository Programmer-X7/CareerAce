package com.bcet.mock_test_service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateQuestionDTO {
    private String questionText;
    private String explanation;
    private List<CreateOptionDTO> options;
}
