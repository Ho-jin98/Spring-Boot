package com.example.userschedule.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequest {
    private String username;
    private String email;
    @Size(min = 8, max = 20)
    private String password;
}
