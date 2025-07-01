package com.hitta.ContractApp.model;

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

    public List<WorkExperience> work;
    public List<Project> projects;
    public List<Education> education;

    public String techs;
    public String skills;
    public String interests;
}
