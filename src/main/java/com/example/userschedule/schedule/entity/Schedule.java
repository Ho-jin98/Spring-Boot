package com.example.userschedule.schedule.entity;

import com.example.userschedule.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "schedules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String writer;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Schedule(String writer, String title, String content, User user) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public  void update(String writer, String title, String content){
        this.writer = writer;
        this.title = title;
        this.content = content;
    }


}
