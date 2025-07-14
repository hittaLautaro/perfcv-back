package com.hitta.ContractApp.service;

import com.hitta.ContractApp.dtos.DraftDto;
import com.hitta.ContractApp.mappers.DraftMapper;
import com.hitta.ContractApp.model.Users;
import com.hitta.ContractApp.repo.DraftRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DraftService {

    private final DraftRepo draftRepo;
    private final DraftMapper draftMapper;


    public DraftService(DraftRepo draftRepo, DraftMapper draftMapper){
        this.draftMapper = draftMapper;
        this.draftRepo = draftRepo;
    }

    public List<DraftDto> getDrafts(Users user) {
        var drafts = draftRepo.findAllByUserId((long) user.getId()).orElse(null);
        return drafts == null ? new ArrayList<>() : draftMapper.draftsToDraftsDto(drafts);
    }

    public void deleteDraft(Users user, Long draftId) {
        draftRepo.deleteByUserIdAndId((long) user.getId(), draftId);
    }

    public DraftDto editDraft(Users user, Long id, DraftDto draftDto) {
        var draft = draftRepo.findById(id).orElseThrow(() -> new RuntimeException("Draft not found"));

        if(draft.getUser().getId() != user.getId()) throw new RuntimeException("Unauthorized to edit this draft");

        var mappedDraft = draftMapper.draftDtoToDraft(draftDto, user);

        var savedDraft = draftRepo.save(mappedDraft);

        return draftMapper.draftToDraftDto(savedDraft);
    }
}
