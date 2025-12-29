package com.hitta.ContractApp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetTemplatesResponse {
    private List<TemplateResponse> templates;
    private int page;
    private int limit;
    private int totalItems;
    private int totalPages;
}
