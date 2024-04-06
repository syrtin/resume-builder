package com.syrtin.dataprocessor.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkExperienceDto {

    private Long id;

    @NotEmpty
    private String companyName;

    @NotEmpty
    private String position;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;
}
