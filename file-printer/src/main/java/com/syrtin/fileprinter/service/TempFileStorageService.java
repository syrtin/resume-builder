package com.syrtin.fileprinter.service;

import java.nio.file.Path;

public interface TempFileStorageService {

    Path saveTempFile(byte[] content);
}
