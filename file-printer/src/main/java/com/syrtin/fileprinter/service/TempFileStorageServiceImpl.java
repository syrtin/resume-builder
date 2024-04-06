package com.syrtin.fileprinter.service;

import com.syrtin.fileprinter.config.AppProps;
import com.syrtin.fileprinter.exception.FileStorageException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class TempFileStorageServiceImpl implements TempFileStorageService {

    private final AppProps props;

    private final AsyncFileDeletionService asyncFileDeletionService;

    public Path saveTempFile(byte[] content) {
        try {
            Path dirPath = Paths.get(props.getTempFilesDir());
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String filename = java.util.UUID.randomUUID().toString() + ".pdf";
            Path filePath = dirPath.resolve(filename);
            Files.write(filePath, content);

            asyncFileDeletionService.scheduleDeletion(filePath, 1, TimeUnit.MINUTES);

            return filePath;
        } catch (IOException e) {
            throw new FileStorageException("Failed to save temp file", e);
        }
    }

}