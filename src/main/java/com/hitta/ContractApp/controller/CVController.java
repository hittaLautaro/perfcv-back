package com.hitta.ContractApp.controller;

import com.hitta.ContractApp.dtos.CvFormDto;
import com.hitta.ContractApp.service.CVGeneratorService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cv/")
public class CVController {

    private final CVGeneratorService cvService;

    public CVController(CVGeneratorService cvService) {
        this.cvService = cvService;
    }

    @PostMapping("/generate-doc")
    public ResponseEntity<?> generateDoc(@RequestBody CvFormDto form) {
        try{
            byte[] docxBytes = cvService.generateDoc(form);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cv.docx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(docxBytes);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @PostMapping("/generate-pdf")
    public ResponseEntity<?> generatePdf(@RequestBody CvFormDto form) {
        try{
            byte[] pdfBytes = cvService.generatePdf(form);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=cv.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

}
