package com.syrtin.fileprinter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkExperienceDto {

    private Long id;

    private String companyName;

    private String position;

    private LocalDate startDate;

    private LocalDate endDate;
}
