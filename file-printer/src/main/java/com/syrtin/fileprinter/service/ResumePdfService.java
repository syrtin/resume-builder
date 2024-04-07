package com.syrtin.fileprinter.service;

import com.syrtin.fileprinter.dto.ResumePrintRequest;

public interface ResumePdfService {

    String generateResumePdf(ResumePrintRequest request);
}
