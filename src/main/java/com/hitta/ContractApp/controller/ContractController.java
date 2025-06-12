package com.hitta.ContractApp.controller;

import com.hitta.ContractApp.service.ContractService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService){
        this.contractService = contractService;
    }


    @PostMapping("/upload")
    public ResponseEntity<List<String>> upload(@RequestParam("file") MultipartFile file) {
        try {
            List<String> fields = contractService.upload(file);
            return ResponseEntity.ok(fields);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }



    @PostMapping("/download")
    public ResponseEntity<Void> download(){
        return ResponseEntity.ok().build();
    }
}
