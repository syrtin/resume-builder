package com.syrtin.dataprocessor.service;

import com.syrtin.dataprocessor.dto.PhotoDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {

    void savePhoto(MultipartFile file);

    PhotoDto getById(Long id);

    List<PhotoDto> getUserPhotos();

    void deleteById(Long id);
}
