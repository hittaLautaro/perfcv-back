package com.hitta.ContractApp.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkExperience {
    public String name;
    public String since;
    public String till;
    public String role;
    public String modalityOrCity;
    public String description;
    public String responsibility;
    public String technologies;
}