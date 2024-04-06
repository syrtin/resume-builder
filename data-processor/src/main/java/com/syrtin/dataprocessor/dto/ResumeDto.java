package com.syrtin.dataprocessor.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDto {

    private Long id;

    @NotEmpty
    private String fullName;

    @NotEmpty
    private String email;

    @NotEmpty
    private String skills;

    @NotEmpty
    private String objective;

    private String interests;

    @Valid
    private List<EducationDto> educations = new ArrayList<>();

    @Valid
    private List<WorkExperienceDto> workExperiences = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Long photoId;

    private Long templateId;
}
