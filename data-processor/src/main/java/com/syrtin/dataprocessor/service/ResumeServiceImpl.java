package com.syrtin.dataprocessor.service;

import com.syrtin.dataprocessor.dto.*;
import com.syrtin.dataprocessor.model.Education;
import com.syrtin.dataprocessor.model.Resume;
import com.syrtin.dataprocessor.model.Template;
import com.syrtin.dataprocessor.model.WorkExperience;
import com.syrtin.dataprocessor.repository.PhotoRepository;
import com.syrtin.dataprocessor.repository.ResumeRepository;
import com.syrtin.dataprocessor.repository.TemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class ResumeServiceImpl implements ResumeService {

    private final ModelMapper modelMapper;

    private final ResumeRepository resumeRepository;

    private final PhotoRepository photoRepository;

    private final PhotoService photoService;

    private final TemplateRepository templateRepository;

    private final UserService userService;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public ResumeDto findById(Long id) {
        log.info("Searching for resume with ID: {}", id);
        return modelMapper.map(resumeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Resume not found with ID: {}", id);
                    return new EntityNotFoundException("Resume not found with ID: " + id);
                }), ResumeDto.class);
    }

    @Override
    public List<ResumeDto> findByCurrentUser() {
        var user = userService.getCurrentUser();
        log.info("Fetching resumes for user: {}", user.getUsername());
        return resumeRepository.findAllByUser(userService.getCurrentUser()).stream()
                .map(r -> modelMapper.map(r, ResumeDto.class))
                .toList();
    }

    @Override
    public ResumeDto insert(ResumeDto resumeDto) {
        var user = userService.getCurrentUser();
        log.info("Inserting a new resume for user: {}", user.getUsername());

        ResumeDto insertedResumeDto = null;
        try {
            var educations = resumeDto.getEducations().stream()
                    .map(e -> modelMapper.map(e, Education.class))
                    .toList();
            var workExperience = resumeDto.getWorkExperiences().stream()
                    .map(we -> modelMapper.map(we, WorkExperience.class))
                    .toList();

            var photo = resumeDto.getPhotoId() == null ? null : photoRepository.findById(resumeDto.getPhotoId())
                    .orElseThrow(() -> new EntityNotFoundException("Photo not found"));

            var template = modelMapper.map(templateIdToTemplateDto(resumeDto.getTemplateId()), Template.class);

            var resume = new Resume(0L, resumeDto.getFullName(), resumeDto.getEmail(), resumeDto.getSkills(),
                    resumeDto.getObjective(), resumeDto.getInterests(), educations, workExperience,
                    LocalDateTime.now(), LocalDateTime.now(), user, photo, template);
            insertedResumeDto = modelMapper.map(resumeRepository.save(resume), ResumeDto.class);
        } catch (EntityNotFoundException e) {
            log.error("Insertion failed: {}", e.getMessage());
            throw e;
        }

        return insertedResumeDto;
    }

    @Override
    public ResumeDto updateResume(Long resumeId, ResumeDto resumeDto) {
        log.info("Updating resume with ID: {}", resumeId);

        ResumeDto updatedResumeDto = null;
        try {
            Resume existingResume = resumeRepository.findById(resumeId)
                    .orElseThrow(() -> new EntityNotFoundException("Resume with ID " + resumeId + " not found."));

            existingResume.setFullName(resumeDto.getFullName());
            existingResume.setEmail(resumeDto.getEmail());
            existingResume.setSkills(resumeDto.getSkills());
            existingResume.setObjective(resumeDto.getObjective());
            existingResume.setInterests(resumeDto.getInterests());
            // existingResume.setPhoto(photoRepository.findById(resumeDto.getPhotoId()).orElse(null));

            updateEducations(existingResume, resumeDto.getEducations());

            updateWorkExperiences(existingResume, resumeDto.getWorkExperiences());

            existingResume.setModifiedAt(LocalDateTime.now());

            var photo = resumeDto.getPhotoId() == null ? null : photoRepository.findById(resumeDto.getPhotoId())
                    .orElseThrow(() -> new EntityNotFoundException("Photo not found"));

            var template = templateRepository.findById(resumeDto.getTemplateId())
                    .orElseThrow(() -> new EntityNotFoundException("Template not found"));

            existingResume.setPhoto(photo);

            existingResume.setTemplate(template);

            var resume = resumeRepository.save(existingResume);

            updatedResumeDto = modelMapper.map(resumeRepository.save(resume), ResumeDto.class);
        } catch (EntityNotFoundException e) {
            log.error("Update failed: {}", e.getMessage());
            throw e;
        }

        return updatedResumeDto;
    }

    @Override
    public ResumePrintResponse print(Long resumeId) {
        log.info("Printing resume with ID: {}", resumeId);

        ResumePrintResponse resumePrintResponse = null;
        try {
            var resume = findById(resumeId);

            var photoData = resume.getPhotoId() == null ? null : photoService.getById(resume.getPhotoId()).getPhotoData();

            var template = templateRepository.findById(resume.getTemplateId())
                    .orElseThrow(() -> new EntityNotFoundException("Template not found"));

            ResumePrintRequest printRequest = new ResumePrintRequest(
                    resume.getId(),
                    resume.getFullName(),
                    resume.getEmail(),
                    resume.getSkills(),
                    resume.getObjective(),
                    resume.getInterests(),
                    resume.getEducations(),
                    resume.getWorkExperiences(),
                    photoData,
                    template.getName());

            resumePrintResponse = (ResumePrintResponse) rabbitTemplate.convertSendAndReceive(printRequest);
        } catch (EntityNotFoundException e) {
            log.error("Printing failed: {}", e.getMessage());
            throw e;
        } catch (AmqpException e) {
            log.error("Printing failed due to rabbitMq request: {}", e.getMessage());
            throw e;
        }

        return resumePrintResponse;
    }

    private void updateEducations(Resume resume, List<EducationDto> educationsDto) {
        resume.getEducations().removeIf(education -> educationsDto.stream()
                .noneMatch(dto -> dto.getId().equals(education.getId())));

        for (EducationDto dto : educationsDto) {
            Education education;
            if (dto.getId() != null) {
                education = resume.getEducations().stream()
                        .filter(e -> dto.getId().equals(e.getId()))
                        .findFirst()
                        .orElse(new Education());
            } else {
                education = new Education();
            }
            education.setDegree(dto.getDegree());
            education.setInstitution(dto.getInstitution());
            education.setYearCompleted(dto.getYearCompleted());
            education.setResume(resume);
            if (education.getId() == null) {
                resume.getEducations().add(education);
            }
        }
    }

    private void updateWorkExperiences(Resume resume, List<WorkExperienceDto> workExperiencesDto) {
        resume.getWorkExperiences().removeIf(workExp -> workExperiencesDto.stream()
                .noneMatch(dto -> dto.getId().equals(workExp.getId())));

        for (WorkExperienceDto dto : workExperiencesDto) {
            WorkExperience workExperience;
            if (dto.getId() != null) {
                workExperience = resume.getWorkExperiences().stream()
                        .filter(we -> dto.getId().equals(we.getId()))
                        .findFirst()
                        .orElse(new WorkExperience());
            } else {
                workExperience = new WorkExperience();
            }
            workExperience.setCompanyName(dto.getCompanyName());
            workExperience.setPosition(dto.getPosition());
            workExperience.setStartDate(dto.getStartDate());
            workExperience.setEndDate(dto.getEndDate());
            workExperience.setResume(resume);
            if (workExperience.getId() == null) {
                resume.getWorkExperiences().add(workExperience);
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting resume with ID: {}", id);
        try {
            resumeRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Deletion failed: {}", e.getMessage());
            throw e;
        }
    }

    private TemplateDto templateIdToTemplateDto(Long templateId) {
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));
        return new TemplateDto(template.getId(), template.getName(), template.getDescription(), template.getImageData());
    }

}
