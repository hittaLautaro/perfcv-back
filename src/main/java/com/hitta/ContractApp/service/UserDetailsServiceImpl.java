package com.hitta.ContractApp.service;

import com.hitta.ContractApp.model.CustomUserDetails;
import com.hitta.ContractApp.model.Users;
import com.hitta.ContractApp.repo.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    public UserDetailsServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Users user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Username not found."));
        if(user == null){
            System.out.println("User "+ email +" not found");
            throw new UsernameNotFoundException("User not found");
        }

        return new CustomUserDetails(user);
    }

}
