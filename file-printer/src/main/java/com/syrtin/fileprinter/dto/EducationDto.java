package com.syrtin.fileprinter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationDto {

    private Long id;

    private String degree;

    private String institution;

    private int yearCompleted;
}
