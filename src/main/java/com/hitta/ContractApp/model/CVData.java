package com.hitta.ContractApp.model;

import com.hitta.ContractApp.dtos.EducationDto;
import com.hitta.ContractApp.dtos.ProjectDto;
import com.hitta.ContractApp.dtos.WorkExperienceDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CVData {
    public String fullName;
    public String contactEmail;
    public String number;
    public String city;
    public String github;
    public String linkedin;

    public List<WorkExperienceDto> work;
    public List<ProjectDto> projects;
    public List<EducationDto> education;

    public String techs;
    public String skills;
    public String interests;
}
