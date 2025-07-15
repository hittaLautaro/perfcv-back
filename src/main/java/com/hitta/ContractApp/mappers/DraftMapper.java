package com.hitta.ContractApp.mappers;

import com.hitta.ContractApp.dtos.DraftDto;
import com.hitta.ContractApp.dtos.UpdateDraftDto;
import com.hitta.ContractApp.model.Draft;
import com.hitta.ContractApp.model.Users;
import com.hitta.ContractApp.repo.TemplateRepo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DraftMapper {

    private final TemplateRepo templateRepo;

    public DraftMapper (TemplateRepo templateRepo) {
        this.templateRepo = templateRepo;
    }

    public List<DraftDto> draftsToDraftsDto(List<Draft> drafts){
        return drafts.stream().map(this::draftToDraftDto).toList();
    }

    public DraftDto draftToDraftDto(Draft draft){
        return DraftDto.builder()
                .id(draft.getId())
                .cvFormDto(draft.getForm())
                .title(draft.getTitle())
                .createdAt(draft.getCreatedAt())
                .updatedAt(draft.getUpdatedAt())
                .userId((long) draft.getUser().getId())
                .selectedTemplateId(draft.getTemplate().getId())
                .templateDisplayName(draft.getTemplate().getDisplayName())
                .build();
    }

    public Draft draftDtoToDraft(DraftDto draftDto, Users user) {
        var template = templateRepo.findById(draftDto.getSelectedTemplateId()).orElseThrow(() -> new RuntimeException("Selected template doesn't exist"));

        return Draft.builder()
                .id(draftDto.getId())
                .form(draftDto.getCvFormDto())
                .title(draftDto.getTitle())
                .user(user)
                .updatedAt(draftDto.getUpdatedAt())
                .createdAt(draftDto.getCreatedAt())
                .template(template)
                .build();
    }
}
