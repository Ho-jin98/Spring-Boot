package com.example.userschedule.comment.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateCommentRequest {
    @Size(min = 5, max = 50, message = "5~50글자를 입력해주세요.")
    private String reply;
}
