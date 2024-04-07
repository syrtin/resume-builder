package com.syrtin.dataprocessor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@NoArgsConstructor
public class TemplateDto {
    private Long id;

    private String name;

    private String imageData;

    private String description;

    public TemplateDto(Long id, String name, String description, String imageData) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageData = imageData;
    }

    public TemplateDto(Long id, String name, String description, byte[] imageData) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageData = Base64.getEncoder().encodeToString(imageData);
    }

    public void setImageData(byte[] imageData) {
        this.imageData = Base64.getEncoder().encodeToString(imageData);
    }
}
