package com.hitta.ContractApp.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateDraftDto {
    private String title;
    private CvFormDto cvFormDto;
    private Long selectedTemplateId;
}
