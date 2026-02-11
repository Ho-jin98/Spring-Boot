package com.example.userschedule.comment.repository;

import com.example.userschedule.comment.entity.Comment;
import com.example.userschedule.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByScheduleIdAndIsDeletedFalse(Long scheduleId);
}
