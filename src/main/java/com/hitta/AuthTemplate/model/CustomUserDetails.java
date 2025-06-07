package com.hitta.AuthTemplate.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    @Getter
    private final String email;
    @Getter
    private final Users user;
    private final String password;
    private final boolean accountLocked;
    private final boolean isEnabled;
    private final List<GrantedAuthority> authorities;


    public CustomUserDetails(Users user) {
        this.user = user;
        this.email = user.getEmail();
        this.isEnabled = user.isEnabled();
        this.password = user.getPassword();
        this.accountLocked = user.isAccountLocked();
        this.authorities = List.of(new SimpleGrantedAuthority("USER"));
    }

    public Integer getId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled(){
        return isEnabled;
    }

}