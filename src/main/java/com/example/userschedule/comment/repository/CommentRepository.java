package com.example.userschedule.comment.repository;

import com.example.userschedule.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
//TODO ORM(Object-Relational Mapping) > 자바언어를 적은 것들을 쿼리로 자동으로 변경해줌
// 대표적인 ORM이 JPA
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByScheduleIdAndIsDeletedFalse(Long scheduleId);
    //TODO SELECT * FROM comments WHERE schedule_id = :scheduleId AND is_deleted = false;

    void deleteAllByScheduleId(Long scheduleId);
    //TODO DELETE FROM comment WHERE schedule_id = :schedule_id; >> 이렇게 자동으로 바꿔줌
}
