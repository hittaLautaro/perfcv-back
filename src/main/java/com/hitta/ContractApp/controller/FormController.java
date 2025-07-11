package com.hitta.ContractApp.controller;

import com.hitta.ContractApp.dtos.CvFormDto;
import com.hitta.ContractApp.model.CustomUserDetails;
import com.hitta.ContractApp.model.UserForm;
import com.hitta.ContractApp.model.Users;
import com.hitta.ContractApp.repo.FormRepo;
import com.hitta.ContractApp.service.FormService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/form")
public class FormController {

    private final FormService formService;
    private final FormRepo formRepo;


    public FormController(FormService formService, FormRepo formRepo){
        this.formService = formService;
        this.formRepo = formRepo;

    }

    @GetMapping
    public ResponseEntity<?> getUserForm(@AuthenticationPrincipal CustomUserDetails userDetails){
        try{
            return ResponseEntity.ok(formService.getUserForm(userDetails.getUser()));
        }catch(Exception e){
           return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping
    public ResponseEntity<?> createOrUpdateUserForm(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CvFormDto dto
    ) {
        try {
            return ResponseEntity.ok(formService.createOrUpdateUserForm(userDetails.getUser(), dto));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
