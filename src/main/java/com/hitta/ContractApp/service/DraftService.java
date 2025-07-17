package com.hitta.ContractApp.service;

import com.hitta.ContractApp.dtos.DraftDto;
import com.hitta.ContractApp.dtos.UpdateDraftDto;
import com.hitta.ContractApp.mappers.DraftMapper;
import com.hitta.ContractApp.model.Draft;
import com.hitta.ContractApp.model.Template;
import com.hitta.ContractApp.model.UserForm;
import com.hitta.ContractApp.model.Users;
import com.hitta.ContractApp.repo.DraftRepo;
import com.hitta.ContractApp.repo.TemplateRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DraftService {

    private final DraftRepo draftRepo;
    private final DraftMapper draftMapper;
    private final TemplateRepo templateRepo;


    public DraftService(DraftRepo draftRepo, DraftMapper draftMapper, TemplateRepo templateRepo){
        this.draftMapper = draftMapper;
        this.draftRepo = draftRepo;
        this.templateRepo = templateRepo;
    }

    public List<DraftDto> getDrafts(Long userId) {
        var drafts = draftRepo.findAllByUserId(userId).orElse(null);
        return drafts == null ? new ArrayList<>() : draftMapper.draftsToDraftsDto(drafts);
    }

    @Transactional
    public void deleteDraft(Users user, Long draftId) {
        draftRepo.deleteByUserIdAndId((long) user.getId(), draftId);
    }

    public DraftDto editDraft(Users user, Long id, UpdateDraftDto draftDto) {
        Draft draft = draftRepo.findById(id).orElseThrow(() -> new RuntimeException("Draft not found"));

        if(draft.getUser().getId() != user.getId()) throw new RuntimeException("Unauthorized to edit this draft");

        Template template = templateRepo.findById(draftDto.getSelectedTemplateId()).orElseThrow(() -> new RuntimeException("Template doesn't exist"));

        draft.setForm(draftDto.getCvFormDto());
        draft.setTemplate(template);
        draft.setTitle(draftDto.getTitle());
        draft.setUpdatedAt(LocalDateTime.now());

        var savedDraft = draftRepo.save(draft);

        return draftMapper.draftToDraftDto(savedDraft);
    }

    public void addDraft(Users user, UserForm userForm) {
        Template template = templateRepo.findById(userForm.getForm().getSelectedTemplateId()).orElseThrow(() -> new RuntimeException("Template doesn't exist"));

        var draft = Draft.builder()
                .title("Resume Draft")
                .form(userForm.getForm())
                .template(template)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .build();

        draftRepo.save(draft);
    }
}
