package com.hitta.ContractApp.service;

import com.deepoove.poi.XWPFTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;

@Service
public class CVGeneratorService {

    public byte[] generateCV() throws Exception {
        // Load template from resources
        ClassPathResource templateResource = new ClassPathResource("templates/cv_template_01.docx");

        // Prepare data model
        Map<String, Object> data = new HashMap<>();
        data.put("fullName", "Jane Doe");
        data.put("contactEmail", "jane@example.com");
        data.put("number", "+1 234 567 890");
        data.put("city", "New York");
        data.put("github", "github.com/janedoe");
        data.put("techs", "Java, Spring Boot");
        data.put("skills", "Backend Development, Microservices");
        data.put("interests", "Open Source, Teaching");

        // Work experiences
        List<Map<String, Object>> workList = new ArrayList<>();
        Map<String, Object> job1 = new HashMap<>();
        job1.put("name", "Amazon");
        job1.put("since", "2019");
        job1.put("till", "2023");
        job1.put("role", "Backend Engineer");
        job1.put("modalityOrCity", "Remote");
        job1.put("description", "Built scalable APIs");
        job1.put("responsibility", "Led backend team");
        job1.put("technologies", "Java, AWS, Docker");
        workList.add(job1);

        Map<String, Object> job2 = new HashMap<>();
        job2.put("name", "Spotify");
        job2.put("since", "2016");
        job2.put("till", "2019");
        job2.put("role", "Software Engineer");
        job2.put("modalityOrCity", "Berlin");
        job2.put("description", "Developed recommendation systems");
        job2.put("responsibility", "Collaborated with data scientists");
        job2.put("technologies", "Python, Spark");
        workList.add(job2);

        data.put("work", workList);

        // Projects
        List<Map<String, Object>> projectList = new ArrayList<>();
        Map<String, Object> project1 = new HashMap<>();
        project1.put("name", "Real.");
        project1.put("since", "June, 2024");
        project1.put("till", "present");
        project1.put("description", "A full stack habit tracker that includes a lot of stuff wow");
        project1.put("technologies", "Java, SpringBoot, SpringSecurity, React, TailwindCSS, JavaScript, Java Mail Sender");
        project1.put("github", "https://github.com/hittaLautaro/REAL-habit-tracker");
        projectList.add(project1);

        data.put("projects", projectList);

        // Education - Fixed structure
        List<Map<String, Object>> eduList = new ArrayList<>();
        Map<String, Object> edu1 = new HashMap<>();
        edu1.put("universityName", "MIT");
        edu1.put("graduationMonth", "June");
        edu1.put("graduationYear", "2015");
        edu1.put("degree", "Bachelor");
        edu1.put("major", "Computer Science");
        edu1.put("city", "Cambridge");
        eduList.add(edu1);

        // Add a second education entry to test the loop
        Map<String, Object> edu2 = new HashMap<>();
        edu2.put("universityName", "Harvard");
        edu2.put("graduationMonth", "May");
        edu2.put("graduationYear", "2013");
        edu2.put("degree", "Master");
        edu2.put("major", "Data Science");
        edu2.put("city", "Cambridge");
        eduList.add(edu2);

        data.put("education", eduList);

        // Debug: Print the data structure
        System.out.println("Template data:");
        System.out.println(data);

        try {
            // Render template
            XWPFTemplate template = XWPFTemplate.compile(templateResource.getInputStream()).render(data);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            template.write(out);
            template.close();

            return out.toByteArray();
        } catch (Exception e) {
            System.err.println("Error processing template: " + e.getMessage());
            throw e;
        }
    }



}