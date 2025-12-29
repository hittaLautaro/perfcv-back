package com.hitta.ContractApp.controller;

import com.hitta.ContractApp.dtos.GetTemplatesResponse;
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
    public ResponseEntity<GetTemplatesResponse> getTemplates(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int limit
    ) {
        int safePage = Math.max(page, 1);
        int safeLimit = Math.min(limit, 6);

        var response = templateService.getTemplates(safePage, safeLimit);
        return ResponseEntity.ok(response);
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
