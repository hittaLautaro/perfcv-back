package com.hitta.ContractApp.controller;

import com.hitta.ContractApp.model.Template;
import com.hitta.ContractApp.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/templates")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTemplateController {

    private final TemplateService templateService;

    @PostMapping
    public ResponseEntity<Template> uploadTemplate(
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "categories", required = false) String[] categories,
            @RequestParam(value = "previewImage", required = false) MultipartFile previewImage,
            @RequestParam("pdfFile") MultipartFile pdfFile,
            @RequestParam("docxFile") MultipartFile docxFile,
            @RequestParam(value = "isPremium", required = false) Boolean isPremium,
            @RequestParam(value = "price", required = false) String price
    ) {
        Template template = templateService.uploadTemplate(
                name, description, categories, previewImage, pdfFile, docxFile, isPremium, price
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
