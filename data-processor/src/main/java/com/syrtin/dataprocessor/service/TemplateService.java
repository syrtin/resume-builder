package com.syrtin.dataprocessor.service;

import com.syrtin.dataprocessor.dto.TemplateDto;
import com.syrtin.dataprocessor.model.Template;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface TemplateService {

    TemplateDto saveTemplate(TemplateDto templateDto);

    boolean deleteTemplate(Long id);

    TemplateDto getTemplateById(Long id);

    List<TemplateDto> getAllTemplates();
}
