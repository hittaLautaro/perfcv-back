package com.hitta.AuthTemplate.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserAuthResponse {
    private String email;
    private String password;
    private boolean enabled;
    private boolean accountLocked;
}
