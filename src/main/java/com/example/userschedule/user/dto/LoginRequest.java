package com.example.userschedule.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequest {
    @Email
    private String email;

    @Size(min = 8, max = 20)
    private String password;
}
