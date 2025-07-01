package com.hitta.ContractApp.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    public String name;
    public String since;
    public String till;
    public String description;
    public String technologies;
    public String github;
}
