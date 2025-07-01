package com.hitta.ContractApp.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education {
    public String universityName;
    public String graduationMonth;
    public String graduationYear;
    public String degree;
    public String major;
    public String city;
}