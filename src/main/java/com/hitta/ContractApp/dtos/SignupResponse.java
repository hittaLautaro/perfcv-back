package com.hitta.ContractApp.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignupResponse {
    private String name;
    private String email;
}