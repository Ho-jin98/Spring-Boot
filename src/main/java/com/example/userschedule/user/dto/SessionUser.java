package com.example.userschedule.user.dto;

import lombok.Getter;

//TODO 우리가 지금까지 사용해온 Response DTO는 클라이언트에게 JSON으로 전달되는 데이터를 의미했는데,
// SessionUser DTO는 클라이언트 전달용이 아닌, 서버의 메모리(세션)에 저장되는 용도이다!
// 세션 저장 전용 DTO 또는 인증 정보를 담은 객체로 보는 것이 이해하기 편한것 같다.

@Getter
public class SessionUser {
    private final Long id;
    private final String username;
    private final String email;

    public SessionUser(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
