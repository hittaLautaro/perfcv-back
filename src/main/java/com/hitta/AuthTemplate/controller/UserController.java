package com.hitta.AuthTemplate.controller;

import com.hitta.AuthTemplate.dtos.UserResponse;
import com.hitta.AuthTemplate.model.CustomUserDetails;
import com.hitta.AuthTemplate.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "User non-security related data management endpoints")
public class  UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/")
    @Operation(
            summary = "Get current authenticated user details",
            description = "Fetches the user information of the currently authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved user information",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - user is not authenticated",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"error\": \"Unauthorized\"}"
                            )
                    )
            )
    })
    public ResponseEntity<UserResponse> getUserResponse(
            @Parameter(
                    description = "Authenticated user details",
                    hidden = true
            )
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.findUserResponseById(userDetails.getId()));
    }
}
