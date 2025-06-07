package com.hitta.AuthTemplate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;



@RestController
@RequestMapping("/api/oauth2")
@Tag(name = "OAuth2", description = "OAuth2 authentication endpoints")
public class OAuth2Controller {

    @Value("${BASE_URL}")
    private String baseUrl;

    @GetMapping("/google/authorization-url")
    @Operation(
            summary = "Get Google OAuth2 authorization URL",
            description = "Returns the Google OAuth2 authorization URL"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authorization URL returned successfully")
    })
    public ResponseEntity<Map<String, String>> getGoogleAuthorizationUrl() {
        String authUrl = baseUrl + "/oauth2/authorization/google";

        return ResponseEntity.ok(Map.of(
                "authorizationUrl", authUrl,
                "provider", "google"
        ));
    }
}