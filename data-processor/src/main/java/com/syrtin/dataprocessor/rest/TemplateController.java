package com.syrtin.dataprocessor.rest;

import com.syrtin.dataprocessor.dto.TemplateDto;
import com.syrtin.dataprocessor.exception.TemplateNotFoundException;
import com.syrtin.dataprocessor.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class TemplateController {
    private final TemplateService templateService;

    @Autowired
    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @PostMapping("/api/templates")
    public ResponseEntity<TemplateDto> addTemplate(@RequestBody TemplateDto templateDto) {
        TemplateDto savedTemplateDto = templateService.saveTemplate(templateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTemplateDto);
    }

    @DeleteMapping("/api/templates/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/templates/{id}")
    public ResponseEntity<TemplateDto> getTemplateById(@PathVariable Long id) {
        TemplateDto templateDto = templateService.getTemplateById(id);
        return ResponseEntity.ok(templateDto);
    }

    @GetMapping("/api/templates")
    public ResponseEntity<List<TemplateDto>> getAllTemplates() {
        List<TemplateDto> templates = templateService.getAllTemplates();
        return ResponseEntity.ok(templates);
    }

    @ExceptionHandler(TemplateNotFoundException.class)
    public final ResponseEntity<String> handleTemplateNotFoundException(TemplateNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<String> handleAllExceptions(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
    }
}
