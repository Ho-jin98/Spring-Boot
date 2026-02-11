package com.example.userschedule.comment.controller;

import com.example.userschedule.comment.dto.*;
import com.example.userschedule.comment.service.CommentService;
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
    public ResponseEntity<CreateCommentResponse> createComment(
            @PathVariable Long userId,
            @PathVariable Long scheduleId,
            @Valid @RequestBody CreateCommentRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(userId,scheduleId,request));
    }

    @GetMapping("/users/{userId}/schedules/{scheduleId}/comments/{commentId}")
    public ResponseEntity<GetCommentResponse> getComment(@PathVariable Long scheduleId, @PathVariable Long commentId){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getComment(scheduleId, commentId));
    }

    @GetMapping("/users/{userId}/schedules/{scheduleId}/comments")
    public ResponseEntity<List<GetCommentResponse>> getComments(
            @PathVariable Long scheduleId){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getComments(scheduleId));
    }

    @PutMapping("/users/{userId}/schedules/{scheduleId}/comments/{commentId}")
    public ResponseEntity<UpdateCommentResponse> updateComment(
            @PathVariable Long commentId,
            @PathVariable Long userId,
            @PathVariable Long scheduleId,
            @Valid @RequestBody UpdateCommentRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.update(commentId, userId, scheduleId, request));
    }

    @DeleteMapping("/users/{userId}/schedules/{scheduleId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment (
            @PathVariable Long commentId, @PathVariable Long userId, @PathVariable Long scheduleId){
        commentService.delete(commentId, userId, scheduleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
