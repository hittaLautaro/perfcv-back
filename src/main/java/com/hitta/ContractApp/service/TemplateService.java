package com.hitta.ContractApp.service;

import com.hitta.ContractApp.dtos.TemplateResponse;
import com.hitta.ContractApp.exceptions.ResourceNotFoundException;
import com.hitta.ContractApp.model.Template;
import com.hitta.ContractApp.repo.TemplateRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateService {

    private final TemplateRepo templateRepo;
    private final S3Service s3Service;
    private final PdfToImageService pdfToImageService;

    public List<TemplateResponse> getAllTemplates() {
        List<Template> foundTemplates = templateRepo.findByIsActiveTrue();
        List<TemplateResponse> responses = templatesToTemplateResponses(foundTemplates);
        return responses;
    }

    public TemplateResponse getTemplateById(Long id) {
        Template template = templateRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found with id: " + id));

        return templateToTemplateResponse(template);
    }

    @Transactional
    public String getTemplateDownloadUrl(Long id, String format) {
        Template template = templateRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found with id: " + id));

        template.setDownloadCount(template.getDownloadCount() + 1);
        templateRepo.save(template);

        String s3Key = switch(format.toLowerCase()) {
            case "docx" -> {
                if (template.getTemplateDocxS3Key() == null) {
                    throw new ResourceNotFoundException("DOCX format not available for this template");
                }
                yield template.getTemplateDocxS3Key();
            }
            case "pdf" -> template.getTemplatePdfS3Key();
            default -> throw new IllegalArgumentException("Unsupported format: " + format + ". Supported formats: pdf, docx");
        };

        return s3Service.generatePresignedUrl(s3Key, Duration.ofHours(1));
    }

    @Transactional
    public Template uploadTemplate(
            String name,
            String description,
            MultipartFile pdfFile,
            MultipartFile docxFile
    ) {

        Template template = Template.builder()
                .name(name)
                .description(description)
                .isActive(true)
                .build();

        // Upload PDF to S3
        String pdfKey = s3Service.uploadFile(pdfFile, "templates");
        template.setTemplatePdfS3Key(pdfKey);

        // Upload DOCX to S3 if provided
        if (docxFile != null && !docxFile.isEmpty()) {
            log.info("Uploading DOCX file");
            String docxKey = s3Service.uploadFile(docxFile, "templates");
            template.setTemplateDocxS3Key(docxKey);
        }

        // Upload preview image to S3 (either provided or auto-generated)
        String previewKey;

        log.info("No preview image provided, generating from PDF");
        try {
            byte[] imageBytes = pdfToImageService.convertFirstPageToImage(pdfFile);
            previewKey = s3Service.uploadBytes(imageBytes, "previews", "image/png", ".png");
            log.info("Successfully generated preview from PDF");
        } catch (Exception e) {
            log.error("Failed to generate preview from PDF", e);
            throw new RuntimeException("Failed to generate preview image from PDF: " + e.getMessage(), e);
        }
        template.setPreviewImageS3Key(previewKey);

        return templateRepo.save(template);
    }

    @Transactional
    public void deleteTemplate(Long id) {
        Template template = templateRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found with id: " + id));

        // Delete files from S3
        s3Service.deleteFile(template.getPreviewImageS3Key());
        s3Service.deleteFile(template.getTemplatePdfS3Key());
        if (template.getTemplateDocxS3Key() != null) {
            s3Service.deleteFile(template.getTemplateDocxS3Key());
        }

        // Delete from database
        templateRepo.delete(template);
    }

    @Transactional
    public void deactivateTemplate(Long id) {
        Template template = templateRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found with id: " + id));

        template.setIsActive(false);
        templateRepo.save(template);
    }

    private List<TemplateResponse> templatesToTemplateResponses(List<Template> foundTemplates) {
        return foundTemplates.stream()
                .map(this::templateToTemplateResponse)
                .toList();
    }

    private TemplateResponse templateToTemplateResponse(Template template) {
        var res = TemplateResponse.builder()
                .id(template.getId())
                .name(template.getName())
                .description(template.getDescription())
                .downloadCount(template.getDownloadCount())
                .build();


        // Generate preview url (valid for 15 minutes)
        String previewUrl = s3Service.generatePresignedUrl(template.getPreviewImageS3Key());

        res.setPreviewUrl(previewUrl);

        return res;
    }
}
