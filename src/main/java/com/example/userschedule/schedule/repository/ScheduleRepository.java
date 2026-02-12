package com.example.userschedule.schedule.repository;

import com.example.userschedule.comment.entity.Comment;
import com.example.userschedule.schedule.entity.Schedule;
import com.example.userschedule.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUser(User user);
    List<Schedule> findByUserAndIsDeletedFalse(User user);
    Schedule findByIdAndIsDeletedFalse(Long scheduleId);
}
