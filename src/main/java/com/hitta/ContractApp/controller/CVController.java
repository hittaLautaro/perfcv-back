package com.hitta.ContractApp.controller;

import com.hitta.ContractApp.dtos.CvFormDto;
import com.hitta.ContractApp.model.CustomUserDetails;
import com.hitta.ContractApp.service.CVGeneratorService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cv/")
public class CVController {

    private final CVGeneratorService cvService;

    public CVController(CVGeneratorService cvService) {
        this.cvService = cvService;
    }

    @PostMapping("/generate-doc")
    public ResponseEntity<?> generateDoc(@RequestBody CvFormDto form, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try{
            byte[] docxBytes = cvService.generateDoc(userDetails.getUser(), form);
            String fileName = String.format("%s_RESUME.docx", form.getFullName().replace(" ", "_").toUpperCase());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(docxBytes);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @PostMapping("/generate-pdf")
    public ResponseEntity<?> generatePdf(@RequestBody CvFormDto form, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try{
            byte[] pdfBytes = cvService.generatePdf(userDetails.getUser(), form);
            String fileName = String.format("%s_RESUME.pdf", form.getFullName().replace(" ", "_").toUpperCase());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
