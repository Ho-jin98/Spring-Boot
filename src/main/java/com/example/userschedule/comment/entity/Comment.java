package com.example.userschedule.comment.entity;

import com.example.userschedule.schedule.entity.Schedule;
import com.example.userschedule.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table("comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reply;
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    public Comment(String reply, User user, Schedule schedule) {
        this.reply = reply;
        this.user = user;
        this.schedule = schedule;
    }

    public void update(String reply){
        this.reply = reply;
    }
    public void deleted(boolean isDeleted){
        this.isDeleted = isDeleted;
    }
}
