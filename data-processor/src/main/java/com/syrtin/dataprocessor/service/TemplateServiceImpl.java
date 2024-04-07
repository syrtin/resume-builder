package com.syrtin.dataprocessor.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.syrtin.dataprocessor.dto.TemplateDto;
import com.syrtin.dataprocessor.exception.TemplateNotFoundException;
import com.syrtin.dataprocessor.model.Template;
import com.syrtin.dataprocessor.repository.TemplateRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TemplateServiceImpl implements TemplateService {
    private final TemplateRepository templateRepository;

    @Autowired
    public TemplateServiceImpl(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @CircuitBreaker(name = "templateService", fallbackMethod = "fallbackSaveTemplate")
    @Override
    public TemplateDto saveTemplate(TemplateDto templateDto) {
        Template template = new Template();
        template.setName(templateDto.getName());
        template.setImageData(Base64.getDecoder().decode(templateDto.getImageData()));
        Template savedTemplate = templateRepository.save(template);
        return convertToDto(savedTemplate);
    }

    @CircuitBreaker(name = "templateService", fallbackMethod = "fallbackDeleteTemplate")
    @Override
    public boolean deleteTemplate(Long id) {
        if (!templateRepository.existsById(id)) {
            throw new TemplateNotFoundException(HttpStatus.NOT_FOUND, "Template not found with id: " + id);
        }
        templateRepository.deleteById(id);
        return true;
    }

    @CircuitBreaker(name = "templateService", fallbackMethod = "fallbackGetTemplateById")
    @Override
    public TemplateDto getTemplateById(Long id) {
        return templateRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new TemplateNotFoundException(HttpStatus.NOT_FOUND, "Template not found with id: " + id));
    }

    @CircuitBreaker(name = "templateService", fallbackMethod = "fallbackGetAllTemplates")
    @Override
    public List<TemplateDto> getAllTemplates() {
        return templateRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private TemplateDto fallbackSaveTemplate(TemplateDto templateDto, Exception e) {
        log.error("Error saving template: ", e);
        return new TemplateDto();
    }

    private boolean fallbackDeleteTemplate(Long id, Exception e) {
        log.error("Error deleting template with id {}: ", id, e);
        return false;
    }

    private TemplateDto fallbackGetTemplateById(Long id, Exception e) {
        log.error("Error fetching template with id {}: ", id, e);
        return new TemplateDto();
    }

    private List<TemplateDto> fallbackGetAllTemplates(Exception e) {
        log.error("Error fetching all templates: ", e);
        return Collections.emptyList();
    }
    
    private TemplateDto convertToDto(Template template) {
        return new TemplateDto(template.getId(), template.getName(), template.getDescription(), Base64.getEncoder().encodeToString(template.getImageData()));
    }
}
