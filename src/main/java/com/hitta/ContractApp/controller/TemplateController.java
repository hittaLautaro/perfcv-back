package com.hitta.ContractApp.controller;

import com.hitta.ContractApp.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Map<String, String>> getDownloadUrl(
            @PathVariable Long id,
            @RequestParam(defaultValue = "pdf") String format
    ) {
        String downloadUrl = templateService.getTemplateDownloadUrl(id, format);
        return ResponseEntity.ok(Map.of("downloadUrl", downloadUrl));
    }
}
