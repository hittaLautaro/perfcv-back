package com.hitta.ContractApp.controller;

import com.hitta.ContractApp.model.CustomUserDetails;
import com.hitta.ContractApp.service.DraftService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/drafts")
public class DraftController {

    private final DraftService draftService;

    public DraftController(DraftService draftService){
        this.draftService = draftService;
    }

    @GetMapping
    public ResponseEntity<?> getDrafts(@AuthenticationPrincipal CustomUserDetails userDetails){
        try{
            return ResponseEntity.ok(draftService.getDrafts(userDetails.getUser()));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDraft(@AuthenticationPrincipal CustomUserDetails userDetails, @NotNull Long draftId){
        try{
            draftService.deleteDraft(userDetails.getUser(), draftId);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
