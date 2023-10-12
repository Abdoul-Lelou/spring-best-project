package com.abdourahmane.spring_security.dto;

import com.abdourahmane.spring_security.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AuthenticationRequest extends User {
    public String email;
    public String password;
}
