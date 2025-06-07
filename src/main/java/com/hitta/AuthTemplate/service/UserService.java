package com.hitta.AuthTemplate.service;

import com.hitta.AuthTemplate.dtos.UserResponse;
import com.hitta.AuthTemplate.model.Users;
import com.hitta.AuthTemplate.repo.UserRepo;
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
