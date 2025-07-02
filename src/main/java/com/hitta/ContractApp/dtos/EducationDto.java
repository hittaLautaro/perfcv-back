package com.hitta.ContractApp.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationDto {
    @NotBlank(message = "University name is required")
    private String universityName;

    @NotBlank(message = "Major name is required")
    private String major;

    @NotBlank(message = "Degree is required")
    private String degree;

    @NotBlank(message = "Graduation date is required")
    private String graduationDate;

    @NotBlank(message = "City is required")
    private String city;
}