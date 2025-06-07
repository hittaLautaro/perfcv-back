package com.hitta.AuthTemplate.controller;

import com.hitta.AuthTemplate.exceptions.AlreadyVerifiedException;
import com.hitta.AuthTemplate.exceptions.ExpiredTokenException;
import com.hitta.AuthTemplate.exceptions.InvalidTokenException;
import com.hitta.AuthTemplate.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@Tag(name = "Verification", description = "Email verification endpoints")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService){
        this.verificationService = verificationService;
    }

    @PostMapping("/verify")
    @Operation(
            summary = "Verify account using token",
            description = "Verifies a user's account based on the provided verification token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account verified successfully"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Account is already verified",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"already_verified\""))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid token provided",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"invalid_token\""))),
            @ApiResponse(
                    responseCode = "410",
                    description = "Verification token has expired",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"token_expired\""))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"unexpected_error\"")))
    })
    public ResponseEntity<?> verify(
            @Parameter(description = "Verification token", required = true)
            @RequestParam String token,
            @Parameter(hidden = true) HttpServletResponse response
    ) {
        try {
            verificationService.verifyAccountWithToken(response, token);
            return ResponseEntity.ok("verified");
        } catch (AlreadyVerifiedException ave) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("already_verified");
        } catch (InvalidTokenException ite) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid_token");
        } catch (ExpiredTokenException ete) {
            return ResponseEntity.status(HttpStatus.GONE).body("token_expired");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("unexpected_error");
        }
    }


    @PostMapping("/delete")
    @Operation(
            summary = "Delete account using token",
            description = "Deletes a user's account based on the provided deletion token."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Account deleted successfully",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Your account has been deleted.\""))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid token provided",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"invalid_token\""))),
            @ApiResponse(
                    responseCode = "410",
                    description = "Deletion token has expired",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"token_expired\""))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"unexpected_error\"")))
    })
    public ResponseEntity<?> delete(
            @Parameter(description = "Deletion token", required = true)
            @RequestParam String token,
            @Parameter(hidden = true) HttpServletResponse response
    ) {
        try {
            verificationService.deleteAccountWithToken(response, token);
            return ResponseEntity.ok("Your account has been deleted.");
        } catch (InvalidTokenException ite) {
            System.out.println("InvalidTokenException: " + ite.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid_token");
        } catch (ExpiredTokenException ete) {
            System.out.println("ExpiredTokenException: " + ete.getMessage());
            return ResponseEntity.status(HttpStatus.GONE).body("token_expired");
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("unexpected_error");
        }
    }

    @PostMapping("/resend")
    @Operation(
            summary = "Resend verification email",
            description = "Resends the verification email for a user identified by the provided token."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Verification email resent successfully",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"A new verification email has been sent.\""))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Failed to resend verification email",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<?> resend(
            @Parameter(description = "Token to identify the user", required = true)
            @RequestParam String token
    ) {
        try {
            verificationService.resendVerificationEmail(token);
            return ResponseEntity.ok("A new verification email has been sent.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
