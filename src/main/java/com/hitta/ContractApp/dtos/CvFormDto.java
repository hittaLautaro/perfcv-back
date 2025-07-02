package com.hitta.ContractApp.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CvFormDto {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;

    private String phone;

    private String city;

    @NotBlank(message = "Summary is required")
    private String summary;

    private List<WorkExperienceDto> experiences;
    private List<EducationDto> educationList;
    private List<ProjectDto> projects;

    @NotNull(message = "Template selection is required")
    private Long selectedTemplateId;

    private List<String> skills;
    private List<String> languages;

    private String linkedin;
    private String github;
}
