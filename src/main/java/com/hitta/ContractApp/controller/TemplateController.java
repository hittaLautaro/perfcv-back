package com.hitta.ContractApp.controller;

import com.hitta.ContractApp.dtos.TemplateResponse;
import com.hitta.ContractApp.model.CustomUserDetails;
import com.hitta.ContractApp.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @GetMapping
    public ResponseEntity<List<TemplateResponse>> getAllTemplates() {
        List<TemplateResponse> templates = templateService.getAllTemplates();
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateResponse> getTemplateById(@PathVariable Long id) {
        TemplateResponse template = templateService.getTemplateById(id);
        return ResponseEntity.ok(template);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Map<String, String>> getDownloadUrl(
            @PathVariable Long id,
            @RequestParam(defaultValue = "pdf") String format,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        String downloadUrl = templateService.getTemplateDownloadUrl(id, format);
        return ResponseEntity.ok(Map.of("downloadUrl", downloadUrl));
    }
}
