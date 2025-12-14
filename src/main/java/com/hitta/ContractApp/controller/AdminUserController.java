package com.hitta.ContractApp.controller;

import com.hitta.ContractApp.exceptions.ResourceNotFoundException;
import com.hitta.ContractApp.model.Role;
import com.hitta.ContractApp.model.Users;
import com.hitta.ContractApp.repo.UserRepo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserRepo userRepo;

    @PatchMapping("/{id}/role")
    @Operation(summary = "Update user role (ADMIN only)")
    public ResponseEntity<Map<String, String>> updateUserRole(
            @PathVariable Long id,
            @RequestParam("role") String role
    ) {
        Users user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        try {
            Role newRole = Role.valueOf(role.toUpperCase());
            user.setRole(newRole);
            userRepo.save(user);

            return ResponseEntity.ok(Map.of(
                    "message", "User role updated successfully",
                    "userId", user.getId().toString(),
                    "email", user.getEmail(),
                    "newRole", newRole.name()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid role. Valid roles are: USER, ADMIN"));
        }
    }

    @PatchMapping("/email/{email}/role")
    @Operation(summary = "Update user role by email (ADMIN only)")
    public ResponseEntity<Map<String, String>> updateUserRoleByEmail(
            @PathVariable String email,
            @RequestParam("role") String role
    ) {
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        try {
            Role newRole = Role.valueOf(role.toUpperCase());
            user.setRole(newRole);
            userRepo.save(user);

            return ResponseEntity.ok(Map.of(
                    "message", "User role updated successfully",
                    "userId", user.getId().toString(),
                    "email", user.getEmail(),
                    "newRole", newRole.name()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid role. Valid roles are: USER, ADMIN"));
        }
    }
}
