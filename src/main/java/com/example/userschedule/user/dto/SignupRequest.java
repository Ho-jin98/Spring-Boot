package com.example.userschedule.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequest {
    //TODO 유저의 이름이 최소 2글자 ~ 최대 10글자가 맞는지 검증
    @Size(min = 2, max = 10)
    private String username;
    //TODO Email형식이 맞는지 검증
    @Email
    private String email;
    //TODO 비밀번호는 최소 8글자 ~ 20글자가 맞는지 검증
    @Size(min = 8, max = 20)
    private String password;
}
