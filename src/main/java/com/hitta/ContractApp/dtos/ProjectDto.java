package com.hitta.ContractApp.dtos;

import lombok.*;

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
    public String technologies;
    public String github;
}
