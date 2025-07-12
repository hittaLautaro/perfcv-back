package com.hitta.ContractApp.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DraftDto {
    private Long id;
    private String title;
    private CvFormDto cvFormDto;
    private Long selectedTemplateId;
    private String templateDisplayName;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
