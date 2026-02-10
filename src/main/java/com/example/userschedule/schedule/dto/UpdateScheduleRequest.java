package com.example.userschedule.schedule.dto;

import lombok.Getter;

@Getter
public class UpdateScheduleRequest {
    private String writer;
    private String title;
    private String content;
}
