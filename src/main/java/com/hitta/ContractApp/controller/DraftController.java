package com.hitta.ContractApp.controller;

import com.hitta.ContractApp.dtos.DraftDto;
import com.hitta.ContractApp.dtos.UpdateDraftDto;
import com.hitta.ContractApp.model.CustomUserDetails;
import com.hitta.ContractApp.service.DraftService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
            return ResponseEntity.ok(userDetails.getUser().getDrafts());
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editDraft(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("id") Long id, @RequestBody UpdateDraftDto draftDto){
        try{

            return ResponseEntity.ok(draftService.editDraft(userDetails.getUser(), id, draftDto));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDraft(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("id") Long id){
        try{
            draftService.deleteDraft(userDetails.getUser(), id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }
}
