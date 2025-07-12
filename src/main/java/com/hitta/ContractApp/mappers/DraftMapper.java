package com.hitta.ContractApp.mappers;

import com.hitta.ContractApp.dtos.DraftDto;
import com.hitta.ContractApp.model.Draft;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DraftMapper {

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
                .selectedTemplateId(draft.getSelectedTemplate().getId())
                .templateDisplayName(draft.getSelectedTemplate().getDisplayName())
                .build();
    }
}
