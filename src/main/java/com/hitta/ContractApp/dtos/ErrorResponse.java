package com.hitta.ContractApp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private int status;              // HTTP status code (400, 401, 404, etc.)
    private String error;            // Error type (e.g., "Bad Request")
    private String message;          // User-friendly message
    private String path;             // Request path that caused the error
    private LocalDateTime timestamp; // When the error occurred
}
