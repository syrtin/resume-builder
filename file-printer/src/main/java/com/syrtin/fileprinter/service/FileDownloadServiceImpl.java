package com.syrtin.fileprinter.service;

import com.syrtin.fileprinter.exception.DownloadFileException;
import jakarta.servlet.ServletContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;


@Slf4j
@Service
@AllArgsConstructor
public class FileDownloadServiceImpl implements FileDownloadService {

    private final Path fileStorageLocation = Paths.get("temp");

    private final ServletContext servletContext;

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                log.info("Loaded file as resource: {}", fileName);
                return resource;
            } else {
                log.error("File not found: {}", fileName);
                throw new DownloadFileException("File not found " + fileName);
            }
        } catch (MalformedURLException e) {
            log.error("File path is incorrect: {}", fileName, e);
            throw new DownloadFileException("File path is incorrect " + fileName, e);
        }
    }

    @Override
    public String getContentType(Resource resource) {
        String contentType = "application/octet-stream";
        try {
            String filePath = resource.getFile().getAbsolutePath();
            contentType = servletContext.getMimeType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            log.info("Determined content type: {} for {}", contentType, resource.getFilename());
        } catch (IOException e) {
            log.warn("Could not determine file type. Defaulting to {}", contentType, e);
        }
        return contentType;
    }
}
