package com.hitta.ContractApp.service;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.hitta.ContractApp.dtos.CvFormDto;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class CVGeneratorService {

    public byte[] generatePdf(CvFormDto form) throws IOException, InterruptedException {
        byte[] docBytes = generateCv(form);
        return convertDocxToPdf(docBytes);
    }

    public byte[] generateDoc(CvFormDto form) throws IOException {
        return generateCv(form);
    }

    public byte[] convertDocxToPdf(byte[] docxBytes) throws IOException, InterruptedException {
        // 1. Create temp dir
        Path tempDir = Files.createTempDirectory("pdfgen");
        File outputDir = tempDir.toFile();

        // 2. Create temp docx file
        File tempDocxFile = new File(outputDir, "temp_doc.docx");
        Files.write(tempDocxFile.toPath(), docxBytes);

        // 3. Run LibreOffice CLI to convert to PDF
        ProcessBuilder pb = new ProcessBuilder(
                "C:\\Program Files\\LibreOffice\\program\\soffice.exe",
                "--headless", "--convert-to", "pdf",
                tempDocxFile.getAbsolutePath(),
                "--outdir", outputDir.getAbsolutePath()
        );
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor();

        // 4. Read generated PDF
        String pdfFileName = tempDocxFile.getName().replaceFirst("\\.docx$", ".pdf");
        File pdfFile = new File(outputDir, pdfFileName);

        if (!pdfFile.exists()) {
            throw new FileNotFoundException("PDF conversion failed, output not found.");
        }

        byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());

        // 5. Cleanup
        tempDocxFile.delete();
        pdfFile.delete();
        tempDir.toFile().delete();

        return pdfBytes;
    }


    private byte[] generateCv(CvFormDto form) throws IOException {
        ClassPathResource templateResource = new ClassPathResource("templates/cv_simple_template");

        Map<String, Object> data = new HashMap<>();
        data.put("me_fullName", form.getFullName());
        data.put("me_contactEmail", form.getEmail());
        data.put("me_number", form.getPhone());
        data.put("me_city", (form.getCity() != null && !form.getCity().isEmpty()) ? form.getCity() : null);
        data.put("me_github", (form.getGithub() != null && !form.getGithub().isEmpty()) ? form.getGithub() : null);
        data.put("me_technologies", form.getSkills() != null ? String.join(", ", form.getSkills()) : null);
        data.put("me_languages", form.getSkills() != null ? String.join(", ", form.getLanguages()): null);

        // Work experience
        List<Map<String, Object>> workList = new ArrayList<>();
        if (form.getExperiences() != null) {
            for (var exp : form.getExperiences()) {
                Map<String, Object> job = new HashMap<>();
                job.put("job_name", exp.getCompany());
                job.put("job_since", exp.getStartDate());
                job.put("job_till", exp.getEndDate() != null ? exp.getEndDate() : "Present");
                job.put("job_role", exp.getTitle());
                job.put("job_modalityOrCity", exp.getLocation() != null ? exp.getLocation() : null);
                job.put("job_description", exp.getDescription() != null ? exp.getDescription() : null);
                job.put("job_technologies", exp.getTechnologies() != null ? String.join(", ", exp.getTechnologies()): null);

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
                projectMap.put("technologies", project.getTechnologies() != null ? String.join(", ", project.getTechnologies()): null);
                projectMap.put("github", project.getGithub());
                projectList.add(projectMap);
            }
        }
        System.out.println(projectList);
        data.put("projects", projectList);
        data.put("projects_exists", !projectList.isEmpty());

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