package com.hitta.ContractApp.service;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.hitta.ContractApp.dtos.CvFormDto;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class CVGeneratorService {

    public byte[] generatePdf(CvFormDto form) throws IOException{
        byte[] docBytes = generateCv(form);
        return convertDocToPdf(docBytes);
    }

    public byte[] generateDoc(CvFormDto form) throws IOException {
        return generateCv(form);
    }

    private byte[] convertDocToPdf(byte[] docBytes){
        return docBytes;
    }

    private byte[] generateCv(CvFormDto form) throws IOException {
        ClassPathResource templateResource = new ClassPathResource("templates/cv_template_01.docx");

        Map<String, Object> data = new HashMap<>();
        data.put("me_fullName", form.getFullName());
        data.put("me_contactEmail", form.getEmail());
        data.put("me_number", form.getPhone());
        data.put("me_city", "");
        data.put("me_github", form.getGithub());
        data.put("me_technologies", String.join(", ", form.getSkills() != null ? form.getSkills() : List.of()));
        data.put("me_languages", String.join(", ", form.getLanguages() != null ? form.getLanguages() : List.of()));

        // Work experience
        List<Map<String, Object>> workList = new ArrayList<>();
        if (form.getExperiences() != null) {
            for (var exp : form.getExperiences()) {
                Map<String, Object> job = new HashMap<>();
                job.put("job_name", exp.getCompany());
                job.put("job_since", exp.getStartDate());
                job.put("job_till", exp.getEndDate() != null ? exp.getEndDate() : "Present");
                job.put("job_role", exp.getTitle());
                job.put("job_modalityOrCity", exp.getLocation() != null ? exp.getLocation() : "");
                job.put("job_description", exp.getDescription() != null ? exp.getDescription() : "");
                job.put("job_technologies", String.join(", ", exp.getTechnologies() != null ? exp.getTechnologies() : List.of()));
                workList.add(job);
            }
        }
        System.out.println(workList);
        data.put("work", workList);
        data.put("work_exists", !workList.isEmpty());

        // Education
        List<Map<String, Object>> eduList = new ArrayList<>();
        if (form.getEducationList() != null) {
            for (var edu : form.getEducationList()) {
                Map<String, Object> eduMap = new HashMap<>();
                eduMap.put("universityName", edu.getUniversityName());
                eduMap.put("graduationDate", edu.getGraduationDate());
                eduMap.put("degree", edu.getDegree());
                eduMap.put("major", edu.getMajor());
                eduMap.put("city", edu.getCity());
                eduList.add(eduMap);
            }
        }
        System.out.println(eduList);
        data.put("education", eduList);
        data.put("education_exists", !eduList.isEmpty());


        // Projects
        List<Map<String, Object>> projectList = new ArrayList<>();
        if (form.getProjects() != null) {
            for (var project : form.getProjects()) {
                Map<String, Object> projectMap = new HashMap<>();
                projectMap.put("name", project.getName());
                projectMap.put("since", project.getSince());
                projectMap.put("till", project.getTill());
                projectMap.put("description", project.getDescription());
                projectMap.put("technologies", project.getTechnologies());
                projectMap.put("github", project.getGithub());
                projectList.add(projectMap);
            }
        }
        System.out.println(projectList);
        data.put("projects", projectList);
        data.put("porjects_exists", !projectList.isEmpty());

        try {
            Configure config = Configure.builder()
                    .useDefaultEL(false)
                    .build();
            XWPFTemplate template = XWPFTemplate.compile(templateResource.getInputStream(), config)
                                                .render(data);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            template.write(out);
            template.close();
            return out.toByteArray();
        } catch (Exception e) {
            System.err.println("Error processing template: " + e.getMessage());
            throw new IOException("Failed to generate CV", e);
        }
    }

}