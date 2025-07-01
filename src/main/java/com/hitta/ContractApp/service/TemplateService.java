package com.hitta.ContractApp.service;

import com.hitta.ContractApp.model.Template;
import com.hitta.ContractApp.repo.TemplateRepo;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateService {

    private final TemplateRepo templateRepo;

    public TemplateService(TemplateRepo templateRepo){
        this.templateRepo = templateRepo;
    }

    public List<Template> getTemplates() throws IOException {
        return templateRepo.findAll();
    }
}
