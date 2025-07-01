package com.hitta.ContractApp.controller;

import com.hitta.ContractApp.model.Template;
import com.hitta.ContractApp.service.TemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService){
        this.templateService = templateService;
    }

    @GetMapping
    public ResponseEntity<List<Template>> getTemplates() throws Exception {
        var templates = templateService.getTemplates();
        return ResponseEntity.ok()
                .body(templates);
    }
}
