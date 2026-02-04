package com.example.schedule.repository;

import com.example.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    //TODO Writer가 null이 아닐때 호출하는 메서드
    List<Schedule> findAllByWriterOrderByModifiedAtDesc(String writer);
    //TODO Writer가 null일 때 호출하는 메서드
    List<Schedule> findAllByOrderByModifiedAtDesc();

    //TODO findAllByWriter >> 여기서 By는 SQL의 WHERE절 같은 녀석임,
    // By의 뒤에는 조건필드가 오고, OrderBy 뒤에는 정렬 기준이 온다, Desc는 정렬방향(내림차순)이다.
    // findAllByWriterOrderByModifiedAtDesc(String writer)
    // 해석 : 전체 조회를 할건데, Writer가 일치하는 것만 골라주고, 정렬은 수정일을 기준 내림차순으로 정렬할게
    // 반면, By와 OrderBy사이에 아무것도 없으면 조건필드가 없는것 >> 조건이 없다.
    // findAllByOrderByModifiedAtDesc();
    // 해석 : 전체조회를 할건데, 수정일을 기준으로 내림차순 정렬해줘
    // By는 조건의 시작점이지만, OrderBy는 정렬의 시작점이다. OrderBy는 조건이 아니다!
    // By >> 조건 / OrderBy >> 정렬(조건X)

}
