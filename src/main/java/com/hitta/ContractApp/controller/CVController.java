package com.hitta.ContractApp.controller;

import com.hitta.ContractApp.service.CVGeneratorService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cv/")
public class CVController {

    private final CVGeneratorService cvService;

    public CVController(CVGeneratorService cvService) {
        this.cvService = cvService;
    }

    @GetMapping("/api/cv/download")
    public ResponseEntity<byte[]> downloadCv() throws Exception {
        byte[] fileContent = cvService.generateCV();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"cv.docx\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileContent);
    }
}
