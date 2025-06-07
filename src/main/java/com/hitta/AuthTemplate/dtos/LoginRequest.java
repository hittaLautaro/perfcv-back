package com.hitta.AuthTemplate.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRequest {
    @NotNull(message = "email is mandatory")
    @NotBlank(message = "email is mandatory")
    @Email(message = "email is not formatted")
    private String email;
    @NotNull(message = "password is mandatory")
    @NotBlank(message = "password is mandatory")
    @Size(min = 8, message = "password should be 8 characters long minimum")
    private String password;
}
