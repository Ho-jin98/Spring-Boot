package com.example.userschedule.user.dto;

import lombok.Getter;

@Getter
public class SessionUser {
    private final Long id;
    private final String username;
    private final String email;
    private final String password;

    public SessionUser(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
