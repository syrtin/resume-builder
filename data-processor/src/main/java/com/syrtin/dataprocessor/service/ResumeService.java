package com.syrtin.dataprocessor.service;

import com.syrtin.dataprocessor.dto.*;
import com.syrtin.dataprocessor.model.Photo;


import java.util.List;

public interface ResumeService {

    ResumeDto findById(Long id);

    List<ResumeDto> findByCurrentUser();

    ResumeDto insert(ResumeDto resumeDto);

    ResumeDto updateResume(Long resumeId, ResumeDto resumeDto);

    void deleteById(Long id);

    ResumePrintResponse print(Long resumeId);

}
