package com.example.userschedule.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
@Getter
public class CreateScheduleRequest {
    //TODO 여기 설정해놓은 message 값이 GlobalExceptionHandler에 있는 getDefaultMessage()에 사용된다.
    @NotBlank(message = "이름은 2~10글자 입니다.")
    @Size(min = 2, max = 10)
    private String writer;

    @Size(min = 3, max = 30, message = "제목은 3~30글자 입니다.")
    private String title;

    @Size(min = 1, max = 200, message = "내용은 1~200글자 입니다.")
    private String content;
}
