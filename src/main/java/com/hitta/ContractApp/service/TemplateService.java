package com.hitta.ContractApp.service;

import com.hitta.ContractApp.model.Template;
import com.hitta.ContractApp.repo.TemplateRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateService {

    private final TemplateRepo templateRepo;
    private final S3Service s3Service;

    public List<Map<String, Object>> getAllTemplates() {
        List<Template> templates = templateRepo.findByIsActiveTrue();

        return templates.stream()
                .map(this::mapTemplateWithPreviewUrl)
                .toList();
    }

    public Map<String, Object> getTemplateById(Long id) {
        Template template = templateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        return mapTemplateWithPreviewUrl(template);
    }

    @Transactional
    public String getTemplateDownloadUrl(Long id) {
        Template template = templateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        template.setDownloadCount(template.getDownloadCount() + 1);
        templateRepo.save(template);

        return s3Service.generatePresignedUrl(
                template.getTemplatePdfS3Key(),
                Duration.ofHours(1)
        );
    }

    @Transactional
    public Template uploadTemplate(
            String name,
            String description,
            String category,
            MultipartFile previewImage,
            MultipartFile pdfFile,
            Boolean isPremium,
            String price
    ) {

        Template template = Template.builder()
                .name(name)
                .description(description)
                .category(category)
                .isPremium(isPremium != null ? isPremium : false)
                .price(price != null && !price.trim().isEmpty() ? new java.math.BigDecimal(price) : null)
                .isActive(true)
                .build();

        // Upload files to S3
        String previewKey = s3Service.uploadFile(previewImage, "previews");
        String pdfKey = s3Service.uploadFile(pdfFile, "templates");

        template.setPreviewImageS3Key(previewKey);
        template.setTemplatePdfS3Key(pdfKey);

        return templateRepo.save(template);
    }

    @Transactional
    public void deleteTemplate(Long id) {
        Template template = templateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        // Delete files from S3
        s3Service.deleteFile(template.getPreviewImageS3Key());
        s3Service.deleteFile(template.getTemplatePdfS3Key());

        // Delete from database
        templateRepo.delete(template);
    }

    @Transactional
    public void deactivateTemplate(Long id) {
        Template template = templateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        template.setIsActive(false);
        templateRepo.save(template);
    }

    private Map<String, Object> mapTemplateWithPreviewUrl(Template template) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", template.getId());
        result.put("name", template.getName());
        result.put("description", template.getDescription());
        result.put("category", template.getCategory());
        result.put("isPremium", template.getIsPremium());
        result.put("price", template.getPrice());
        result.put("downloadCount", template.getDownloadCount());
        result.put("createdAt", template.getCreatedAt());

        // Generate preview url (valid for 15 minutes)
        String previewUrl = s3Service.generatePresignedUrl(template.getPreviewImageS3Key());
        result.put("previewUrl", previewUrl);

        return result;
    }
}
