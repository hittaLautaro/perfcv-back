package com.hitta.ContractApp.controller;

import com.hitta.ContractApp.dtos.AuthResponse;
import com.hitta.ContractApp.dtos.LoginRequest;
import com.hitta.ContractApp.dtos.RegisterRequest;
import com.hitta.ContractApp.dtos.UserResponse;
import com.hitta.ContractApp.exceptions.InvalidCredentialsException;
import com.hitta.ContractApp.model.CustomUserDetails;
import com.hitta.ContractApp.model.Users;
import com.hitta.ContractApp.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Auth and users managing endpoints")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Create a new user account with email and password. Returns user details and sets authentication cookies."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User registered successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Users.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid registration data or user already exists",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"error\": \"Email already exists\"}"
                            )
                    )
            )
    })
    public ResponseEntity<?> register(
            @Parameter(
                    description = "User registration details",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RegisterRequest.class)
                    )
            )
            @RequestBody @Valid RegisterRequest request,
            @Parameter(hidden = true) HttpServletResponse response) {
        try {
            Users user = authService.register(request, response);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/authenticate")
    @Operation(
            summary = "Authenticate user",
            description = "Authenticate user with email and password. Returns access token and sets refresh token cookie."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"error\": \"Invalid email or password\"}"
                            )
                    )
            )
    })
    public ResponseEntity<?> authenticate(
            @Parameter(
                    description = "User login credentials",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LoginRequest.class)
                    )
            )
            @RequestBody @Valid LoginRequest request,
            @Parameter(hidden = true) HttpServletResponse response) {
        try {
            AuthResponse authResponse = authService.authenticate(response, request);
            return ResponseEntity.ok()
                    .body(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/change-password")
    @Operation(
            summary = "Change user password",
            description = "Change the password for an authenticated user. Requires current credentials."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Password changed successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Password changed successfully!")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or current password incorrect"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            )
    })
    public ResponseEntity<?> changePassword(
            @Parameter(
                    description = "Current credentials for password change",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LoginRequest.class)
                    )
            )
            @RequestBody @Valid LoginRequest request,
            @Parameter(hidden = true) HttpServletResponse response) {
        try {
            authService.changePassword(request);
            return ResponseEntity.ok("Password changed successfully!");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh access token",
            description = "Generate a new access token using the refresh token stored in cookies."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "New access token generated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Invalid or expired refresh token"
            )
    })
    public ResponseEntity<?> refreshAccessToken(
            @Parameter(
                    description = "Refresh token from cookie",
                    required = true,
                    example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            )
            @CookieValue("refreshToken") String refreshToken,
            @Parameter(hidden = true) HttpServletResponse response) {
        try {
            AuthResponse authResponse = authService.generateAccessToken(refreshToken);
            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Logout user",
            description = "Revoke the refresh token and clear authentication cookies."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User logged out successfully"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error during logout process"
            )
    })
    public ResponseEntity<Void> logout(
            @Parameter(
                    description = "Refresh token from cookie (optional)"
            )
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            @Parameter(hidden = true) HttpServletResponse response) {
        try {
            System.out.println(refreshToken);
            authService.revokeRefreshToken(response, refreshToken);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get current authenticated user",
            description = "Returns the details of the currently authenticated user based on the access token."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved user details"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - access token is missing or invalid"
            )
    })
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserResponse dto = UserResponse.builder()
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .build();

        return ResponseEntity.ok(dto);
    }

}