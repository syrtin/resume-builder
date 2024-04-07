package com.syrtin.fileprinter.service;

import com.syrtin.fileprinter.exception.FileStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AsyncFileDeletionServiceImpl implements AsyncFileDeletionService {

    @Async
    @Override
    public void scheduleDeletion(Path filePath, long delay, TimeUnit timeUnit) {
        try {
            Thread.sleep(timeUnit.toMillis(delay));
            Files.deleteIfExists(filePath);
            log.info("File {} has been deleted", filePath);
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
            throw new FileStorageException("Failed to delete temp file: " + filePath, e);
        }
    }
}
