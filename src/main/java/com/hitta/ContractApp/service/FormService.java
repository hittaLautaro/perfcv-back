package com.hitta.ContractApp.service;

import com.hitta.ContractApp.dtos.CvFormDto;
import com.hitta.ContractApp.model.UserForm;
import com.hitta.ContractApp.model.Users;
import com.hitta.ContractApp.repo.FormRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FormService {

    private final FormRepo formRepo;

    public FormService(FormRepo formRepo){
        this.formRepo = formRepo;
    }

    public CvFormDto getUserForm(Users user) {
        return user.getUserForm().getForm();
    }

    public CvFormDto createOrUpdateUserForm(Users user, CvFormDto formDto) {
        UserForm userForm = user.getUserForm();

        if (userForm == null) {
            userForm = UserForm.builder()
                    .user(user)
                    .form(formDto)
                    .build();
        } else {
            userForm.setForm(formDto);
        }

        UserForm savedForm = formRepo.save(userForm);
        return savedForm.getForm();
    }
}
