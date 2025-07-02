package com.hitta.ContractApp.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class WorkExperienceDto {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Company name is required")
    private String company;

    private String location;

    @NotBlank(message = "Start date is required")
    private String startDate;

    private String endDate;

    private String description;

    private List<String> technologies;
}
