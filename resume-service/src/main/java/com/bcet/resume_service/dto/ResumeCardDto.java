package com.bcet.resume_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResumeCardDto {

    private String resumeId;
    private String templateId;
    private String title;
    private String themeColor;
    
}
