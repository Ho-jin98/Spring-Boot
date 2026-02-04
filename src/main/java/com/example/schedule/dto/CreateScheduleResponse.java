package com.example.schedule.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateScheduleResponse {

    private final Long id;
    private final String title;
    private final String contents;
    private final String writer;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    //TODO 응답을 줄 때는 비밀번호를 제외 (String password)
    public CreateScheduleResponse(Long id, String title, String contents, String writer,LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;

    }
}
