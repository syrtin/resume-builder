package com.syrtin.dataprocessor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationDto {

    private Long id;

    @NotEmpty
    private String degree;

    @NotEmpty
    private String institution;

    @NotNull
    private int yearCompleted;
}
