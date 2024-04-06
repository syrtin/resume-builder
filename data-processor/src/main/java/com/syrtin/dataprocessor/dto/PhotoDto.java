package com.syrtin.dataprocessor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@NoArgsConstructor
public class
PhotoDto {
    private Long id;
    private String photoData;

    public PhotoDto(Long id, String photoData) {
        this.id = id;
        this.photoData = photoData;
    }

    public PhotoDto(Long id, byte[] photoData) {
        this.id = id;
        this.photoData = Base64.getEncoder().encodeToString(photoData);
    }

    public void setPhotoData(byte[] photoData) {
        this.photoData = Base64.getEncoder().encodeToString(photoData);
    }
}
