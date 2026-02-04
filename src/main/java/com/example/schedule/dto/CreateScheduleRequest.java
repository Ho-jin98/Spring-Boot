package com.example.schedule.dto;

import lombok.Getter;

@Getter
public class CreateScheduleRequest {
    //TODO 클라이언트가 보내준 요청
    //TODO 요청에는 비밀번호 포함
    private String title;
    private String contents;
    private String writer;
    private String password;
}
