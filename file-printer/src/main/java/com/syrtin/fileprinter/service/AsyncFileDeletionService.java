package com.syrtin.fileprinter.service;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public interface AsyncFileDeletionService {
    void scheduleDeletion(Path filePath, long delay, TimeUnit timeUnit);
}
