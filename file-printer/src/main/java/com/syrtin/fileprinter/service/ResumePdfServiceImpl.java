package com.syrtin.fileprinter.service;

import com.syrtin.fileprinter.config.AppProps;
import com.syrtin.fileprinter.dto.ResumePrintRequest;
import com.syrtin.fileprinter.exception.PdfGenerationException;
import com.syrtin.fileprinter.model.Template;
import com.syrtin.fileprinter.repository.TemplateRepository;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.util.Base64;

@Service
@AllArgsConstructor
public class ResumePdfServiceImpl implements ResumePdfService {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/yyyy");

    private final TemplateRepository templateRepository;

    private final AppProps appProps;

    private final TempFileStorageService tempFileStorageService;

    public String generateResumePdf(ResumePrintRequest request) {
        try {
            Template template = templateRepository.findTemplateByName(request.getTemplateName())
                    .orElseThrow(() -> new PdfGenerationException("Template not found"));

            JasperReport jasperReport = JasperCompileManager.compileReport(new ByteArrayInputStream(template.getContent().getBytes()));

            String educationsString = request.getEducations().stream()
                    .map(e -> String.format("%s, %s, %d", e.getDegree(), e.getInstitution(), e.getYearCompleted()))
                    .collect(Collectors.joining("; \n"));

            String workExperiencesString = request.getWorkExperiences().stream()
                    .map(w -> String.format("(%s - %s) - %s, %s",
                            w.getStartDate().format(dateFormatter),
                            w.getEndDate() != null ? w.getEndDate().format(dateFormatter) : "Present",
                            w.getPosition(), w.getCompanyName()))
                    .collect(Collectors.joining("; \n"));

            Map<String, Object> parameters = prepareParameters(request, educationsString, workExperiencesString);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            byte[] pdfContent = JasperExportManager.exportReportToPdf(jasperPrint);
            Path tempFilePath = tempFileStorageService.saveTempFile(pdfContent);

            return String.format("%s/download/%s", appProps.getBaseUrl(), tempFilePath.getFileName());
        } catch (JRException | IOException e) {
            throw new PdfGenerationException("Failed to generate PDF", e);
        }
    }

    private Map<String, Object> prepareParameters(ResumePrintRequest request, String educationsString, String workExperiencesString) throws IOException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fullName", request.getFullName());
        parameters.put("email", request.getEmail());
        parameters.put("skills", request.getSkills());
        parameters.put("objective", request.getObjective());
        parameters.put("interests", request.getInterests());
        parameters.put("educationList", educationsString);
        parameters.put("workExperienceList", workExperiencesString);

        if (request.getPhotoData() != null && !request.getPhotoData().isEmpty()) {
            byte[] decodedBytes = Base64.getDecoder().decode(request.getPhotoData());
            try (ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes)) {
                Image image = ImageIO.read(bis);
                parameters.put("photo", image);
            }
        }
        return parameters;
    }
}
