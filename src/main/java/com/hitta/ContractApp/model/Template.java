package com.hitta.ContractApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "templates")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String displayName;

    @NotBlank
    private String filename;
    @NotBlank
    private String filepath;
    private String previewFilename;
    private String previewFilepath;
}
