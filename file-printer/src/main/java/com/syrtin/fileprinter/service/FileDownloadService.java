package com.syrtin.fileprinter.service;

import org.springframework.core.io.Resource;

public interface FileDownloadService {

    Resource loadFileAsResource(String fileName);

    String getContentType(Resource resource);
}
