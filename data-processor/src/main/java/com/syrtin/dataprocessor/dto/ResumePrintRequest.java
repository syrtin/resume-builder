package com.syrtin.dataprocessor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResumePrintRequest implements Serializable {
    private Long id;

    private String fullName;

    private String email;

    private String skills;

    private String objective;

    private String interests;

    private List<EducationDto> educations = new ArrayList<>();

    private List<WorkExperienceDto> workExperiences = new ArrayList<>();

    private String photoData;

    private String templateName;
}
