package com.example.userschedule.schedule.entity;

import com.example.userschedule.comment.entity.Comment;
import com.example.userschedule.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private boolean isDeleted;


    //TODO @ManyToOne >> N:1관계로, 다수 -> 단일을 참조하는 관계이다.
    // 이 어노테이션이 붙은 쪽이 연관관계의 "주인"
    // 여기서 주인은 "권력"의 의미가 아니라 "책임"의 의미이다!, 즉 주인의 표현은 "누가 더 중요한가?"가 아니라,
    // "누가 외래 키(FK)를 관리(등록/수정)하는가?"를 뜻함.
    // "비즈니스의 주인공은 User이지만, DB와의 연결고리(FK)를 손에 쥐고 있는 실무자는 Schedule이다."

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Schedule(String writer, String title, String content, User user) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void update(String writer, String title, String content){
        this.writer = writer;
        this.title = title;
        this.content = content;
    }
    public void deleted(boolean isDeleted){
        this.isDeleted = isDeleted;
    }
}
