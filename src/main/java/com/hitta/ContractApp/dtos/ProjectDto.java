package com.hitta.ContractApp.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDto {
    public String name;
    public String since;
    public String till;
    public String description;
    public List<String> technologies;
    public String github;
}
