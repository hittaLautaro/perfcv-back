package com.hitta.ContractApp.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TemplateResponse {
    private Long id;
    private String name;
    private String description;
    private Integer downloadCount;
    private String previewUrl;
}
