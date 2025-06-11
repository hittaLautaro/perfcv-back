package com.hitta.ContractApp.service;

import com.hitta.ContractApp.dtos.UserResponse;
import com.hitta.ContractApp.model.Users;
import com.hitta.ContractApp.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<Users> getUsers(){
        return userRepo.findAll();
    }

    public UserResponse findUserResponseById(Integer id){
        return userRepo.findUserInfoById(id).orElse(null);
    }

}
