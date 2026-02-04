package com.example.schedule.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "schedules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//TODO @NoArgsConstructor >> 인자가 없는 생성자, 즉 기본 생성자를 만들어 주는 어노테이션
// @AllArgsConstructior >> 모든 필드를 인자로 받는 생성자를 만들어주는 어노테이션
public class Schedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false)
    private String title;
    @Column(length = 100, nullable = false)
    private String contents;
    @Column(length = 50, nullable = false)
    //TODO 작성자를 username 같이 필드값을 생성하지 말고, writer 처럼 직관적으로 필드값을 주자!
    // 그리고 작성자 필드값에 unique = true를 설정해놓으면, 동명이인의 경우 한명은 사용을 못하고,
    // 한 명의 작성자가 여러개의 일정을 만드는게 불가능해진다.
    // updatable = false를 설정하게 되면 수정이 불가능해져서, 사용자가 실수로 이름에 오타를 냈을 때
    // 일정을 다 지우고 처음부터 다시 써야하는 불편함이 생길 수 있다.
    // updatable = false >> 수정 금지,
    // unique = true >> 중복 금지
    private String writer;
    @Column(length = 50, nullable = false)
    private String password;

    public Schedule(String title, String contents, String writer, String password) {
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.password = password;
    }

    //TODO 수정은 일정 제목, 작성자명만 수정 가능, title과 writer 필드만 적용
    public void update(String title, String writer) {
        this.title = title;
        this.writer = writer;
    }
    //TODO update 메서드를 활용해서 제목이나 작성자를 수정하게 되면,
    // createdAt(작성일)의 @Column(updatable = false) 설정 덕분에 DB에 처음 저장된 값이 바뀌지 않고 유지된다.
    // 반면, modifiedAt(수정일)은 @LastModifiedDate 어노테이션이 트랜잭션이 끝나는 시점에 데이터가 바뀐것을 감지하고
    // 현재 시각으로 덮어쓰기를 해준다.
    // Create(저장)을 하면 -> createdAt, modifiedAt 둘 다 현재 시간으로 저장,
    // Update(수정)을 하면 -> modifiedAt만 현재 시간으로 덮어쓰기해서 갱신함
}
