package com.example.userschedule.comment.controller;

import com.example.userschedule.comment.dto.*;
import com.example.userschedule.comment.service.CommentService;
import com.example.userschedule.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private CommentService commentService;

    @PostMapping("/users/{userId}/schedules/{scheduleId}/comments")
    public ResponseEntity<CommonResponse<CreateCommentResponse>> createComment(
            @PathVariable Long userId,
            @PathVariable Long scheduleId,
            @Valid @RequestBody CreateCommentRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(HttpStatus.CREATED, "댓글 생성 성공",
                commentService.save(userId,scheduleId,request)));
    }

    @GetMapping("/users/{userId}/schedules/{scheduleId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<GetCommentResponse>> getComment(@PathVariable Long scheduleId, @PathVariable Long commentId){
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(HttpStatus.OK, "조회 성공",
                commentService.getComment(scheduleId, commentId)));
    }

    @GetMapping("/users/{userId}/schedules/{scheduleId}/comments")
    public ResponseEntity<CommonResponse<List<GetCommentResponse>>> getComments(
            @PathVariable Long scheduleId){
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(HttpStatus.OK, "조회 성공",
                commentService.getComments(scheduleId)));
    }

    @PutMapping("/users/{userId}/schedules/{scheduleId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<UpdateCommentResponse>> updateComment(
            @PathVariable Long commentId,
            @PathVariable Long userId,
            @PathVariable Long scheduleId,
            @Valid @RequestBody UpdateCommentRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(HttpStatus.OK, "변경 완료",
                commentService.update(commentId, userId, scheduleId, request)));
    }

    @DeleteMapping("/users/{userId}/schedules/{scheduleId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment (
            @PathVariable Long commentId, @PathVariable Long userId, @PathVariable Long scheduleId){
        commentService.delete(commentId, userId, scheduleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
