package com.syrtin.dataprocessor.service;

import com.syrtin.dataprocessor.dto.PhotoDto;
import com.syrtin.dataprocessor.exception.PhotoUploadException;
import com.syrtin.dataprocessor.model.Photo;
import com.syrtin.dataprocessor.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final static long MAXIMUM_FILE_SIZE = 1500000L;

    private final com.syrtin.dataprocessor.repository.PhotoRepository photoRepository;

    private final UserService userService;

    public List<PhotoDto> getUserPhotos() {
        User currentUser = userService.getCurrentUser();
        List<Photo> photos = photoRepository.findAllByUser(currentUser);
        return photos.stream().map(photo -> new PhotoDto(photo.getId(), photo.getPhotoData())).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting photo with ID: {}", id);
        try {
            photoRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Deletion failed: {}", e.getMessage());
            throw e;
        }
    }

    public PhotoDto getById(Long id) {
        var photo = photoRepository.findById(id);
        return new PhotoDto(photo.get().getId(), photo.get().getPhotoData());
    }

    public void savePhoto(MultipartFile file) throws PhotoUploadException {
        try {
            checkFile(file);

            byte[] photoData = file.getBytes();
            Photo photo = new Photo();
            photo.setPhotoData(photoData);
            photo.setUser(userService.getCurrentUser());
            photoRepository.save(photo);
        } catch (IOException e) {
            throw new PhotoUploadException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при чтении файла");
        }
    }

    private void checkFile(MultipartFile file) throws PhotoUploadException {
        if (file.isEmpty()) {
            throw new PhotoUploadException(HttpStatus.BAD_REQUEST, "Файл не найден");
        }

        if (file.getSize() > MAXIMUM_FILE_SIZE) {
            throw new PhotoUploadException(HttpStatus.BAD_REQUEST, "Размер файла превышает 1,5 МБ");
        }

        String contentType = file.getContentType();
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType) && !"image/jpg".equals(contentType)) {
            throw new PhotoUploadException(HttpStatus.BAD_REQUEST, "Недопустимый формат файла");
        }
    }
}
