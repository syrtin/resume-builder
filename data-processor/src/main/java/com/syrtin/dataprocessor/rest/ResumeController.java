package com.syrtin.dataprocessor.rest;

import com.syrtin.dataprocessor.dto.ResumeDto;
import com.syrtin.dataprocessor.dto.ResumePrintResponse;
import com.syrtin.dataprocessor.service.ResumeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
public class ResumeController {

    private final ResumeService resumeService;

    @GetMapping("/api/resumes/{id}")
    public ResumeDto getResumeById(@PathVariable Long id) {
        return resumeService.findById(id);
    }

    @GetMapping("/api/resumes")
    public List<ResumeDto> getResumesForCurrentUser() {
        return resumeService.findByCurrentUser();
    }

    @PostMapping("/api/resumes")
    public ResumeDto saveResume(@Valid @RequestBody ResumeDto resumeDto) {
        return resumeService.insert(resumeDto);
    }

    @PutMapping("/api/resumes/{id}")
    public ResumeDto updateResume(@PathVariable long id, @Valid @RequestBody ResumeDto resumeDto) {
        return resumeService.updateResume(id, resumeDto);
    }

    @DeleteMapping("/api/resumes/{id}")
    public void deleteResumeById(@PathVariable long id) {
        resumeService.deleteById(id);
    }

    @GetMapping("/api/resumes/{id}/print")
    public ResumePrintResponse printResume(@PathVariable Long id) {
        return resumeService.print(id);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<String> handleAllExceptions(Exception e) {
        return new ResponseEntity<>("Internal server error. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
