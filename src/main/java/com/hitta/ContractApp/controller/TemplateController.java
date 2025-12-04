package com.hitta.ContractApp.controller;

import com.hitta.ContractApp.model.Template;
import com.hitta.ContractApp.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllTemplates() {
        List<Map<String, Object>> templates = templateService.getAllTemplates();
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTemplateById(@PathVariable Long id) {
        Map<String, Object> template = templateService.getTemplateById(id);
        return ResponseEntity.ok(template);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Map<String, String>> getDownloadUrl(@PathVariable Long id) {
        String downloadUrl = templateService.getTemplateDownloadUrl(id);
        return ResponseEntity.ok(Map.of("downloadUrl", downloadUrl));
    }

    @PostMapping
    public ResponseEntity<Template> uploadTemplate(
                @RequestParam("name") String name,
                @RequestParam(value = "description", required = false) String description,
                @RequestParam(value = "category", required = false) String category,
                @RequestParam("previewImage") MultipartFile previewImage,
                @RequestParam("pdfFile") MultipartFile pdfFile,
                @RequestParam(value = "isPremium", required = false) Boolean isPremium,
                @RequestParam(value = "price", required = false) String price
    ) {
        Template template = templateService.uploadTemplate(
                name, description, category, previewImage, pdfFile, isPremium, price
        );
        return ResponseEntity.ok(template);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateTemplate(@PathVariable Long id) {
        templateService.deactivateTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
