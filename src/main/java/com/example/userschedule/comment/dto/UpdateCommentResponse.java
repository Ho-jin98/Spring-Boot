package com.example.userschedule.comment.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateCommentResponse {
    private final Long id;
    private final String reply;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public UpdateCommentResponse(Long id, String reply, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.reply = reply;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
