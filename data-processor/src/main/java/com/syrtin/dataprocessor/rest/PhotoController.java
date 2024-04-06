package com.syrtin.dataprocessor.rest;

import com.syrtin.dataprocessor.dto.PhotoDto;
import com.syrtin.dataprocessor.exception.PhotoUploadException;
import com.syrtin.dataprocessor.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("api/uploadPhoto")
    public ResponseEntity<String> uploadPhoto(@RequestParam("photo") MultipartFile file) {
        photoService.savePhoto(file);
        return ResponseEntity.ok().body("Файл успешно загружен");
    }

    @GetMapping("api/photos")
    public ResponseEntity<List<PhotoDto>> getUserPhotos() {
        List<PhotoDto> photos = photoService.getUserPhotos();
        return ResponseEntity.ok(photos);
    }

    @GetMapping("api/photos/{id}")
    public ResponseEntity<PhotoDto> getPhotoById(@PathVariable Long id) {
        PhotoDto photoDto = photoService.getById(id);
        return ResponseEntity.ok(photoDto);
    }

    @DeleteMapping("api/photos/{id}")
    public void deleteById(@PathVariable Long id) {
        photoService.deleteById(id);
    }

    @ExceptionHandler(PhotoUploadException.class)
    public ResponseEntity<String> handlePhotoUploadException(PhotoUploadException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Void> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
    }
}
