package com.hitta.AuthTemplate.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RegisterRequest {
    @NotNull(message = "Date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    @NotNull(message = "Email is mandatory")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email is not formatted")
    private String email;
    @NotNull
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, message = "Name length must be more or equal to 2 characters")
    private String name;
    @NotNull(message = "Password is mandatory")
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password length must be more or equal to 8 characters")
    @Size(max = 28, message = "Password length must be less or equal to 28 characters")
    private String password;
    @NotNull
    private String timeZone;
}
