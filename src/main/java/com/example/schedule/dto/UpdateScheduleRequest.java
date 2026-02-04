package com.example.schedule.dto;

import lombok.Getter;

@Getter
public class UpdateScheduleRequest {
    //TODO 클라이언트가 보내준 요청
    // 일정 제목과 작성자만 수정이 가능해서 필드값을 title과 writer만 주고
    // 요청시에는 비밀번호를 같이 전달해줘야 하므로 password도 필드값으로 준다.

    private String title;
    private String writer;
    private String password;

}
