package com.example.schedule.controller;

import com.example.schedule.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.schedule.service.ScheduleService;

import java.util.List;

@RestController
//TODO @RestController REST API를 처리하는 컨트롤러 어노테이션
@RequiredArgsConstructor
//TODO @RequiredArgsConstructor >> 꼭 필요한 필드들만 골라서 생성자의 매개변수로 만들어주는 어노테이션
// Controller나 Service처럼 다른 객체를 주입 받아 써야하는 클래스에서 주로 쓰임. (final)
public class ScheduleController {
    //TODO 상위 계층인 Controller가 Service를 호출
    private final ScheduleService scheduleService;

    //TODO POST 요청은 RequestBody가 있음, >> @RequestBody
    // @RequestBody >> 통역사를 부르는 신호로, JSON을 자바 객체로 바꿔야 겠다는 판단을 해주는 어노테이션
    @PostMapping("/schedules")
    //TODO ResponseEntity >> "HTTP 응답의 전권을 쥐고 있는 마법의 봉투"
    // ResponseEntity와 Entity는 전혀 다른 존재,
    // ResponseEntity는 스프링 프레임 워크에서 제공하는 응답용 클래스인데,
    // 클라이언트에게 응답을 줄 때 CreateScheduleResponse DTO만 전달 하게 되면 데이터와 상태코드 모두 전달이 되긴 하지만,
    // 상태코드가 기본값인 200OK로 고정되어 있음, 그래서 ResponseEntity로 감싸서 보내주게 되면 상태코드를 우리가 강제로 지정할 수 있고,
    // 그 외의 세부사항들을 개발자가 직접 결정해서 데이터와 함께 전달해줄 수 있다.
    public ResponseEntity<CreateScheduleResponse> createSchedule(@RequestBody CreateScheduleRequest request) {
        CreateScheduleResponse result = scheduleService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
    //TODO 단 건 조회의 GET요청은 Body 없이 파라미터나 경로로 전달 >> @PathVariable
    // 클라이언트가 어떤 사용자를 조회할지 URL에 경로에 id값을 전달해줌,
    // 이것을 @PathVariable로 받아 준다.
    @GetMapping("/schedules/{scheduleId}")
    //TODO 여기서의 id값인 scheduleId 변수명이 @PathVariable로 받는 변수명과 일치해야됨! (Long scheduleId)
    public ResponseEntity<GetScheduleResponse> getOneSchedule(@PathVariable Long scheduleId) {
        //TODO 마찬가지로 스프링 프레임워크에서 제공하는 응답용 클래스 ResponseEntity로 감싸서 리턴해준다.
        GetScheduleResponse result = scheduleService.getOne(scheduleId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    //TODO 전체조회를 할 때는 URL에 Id값을 명시해줄 필요가 없음
    @GetMapping("/schedules")
    //TODO 전체 조회를 했을 때는 결과가 10개일 수도 있고, 100개일 수도 있기 때문에, List로 담아준다.
    // List는 결과아 없을 때 빈 배열을 반환해주기 때문에 에러 방지에도 효과가 있다.
    public ResponseEntity<List<GetScheduleResponse>> getAllSchedules(@RequestParam(required = false) String writer) {
        //TODO @RequestParam(required = false) >> writer가 조회 조건으로 포함될 수도 있고, 포함되지 않을 수도 있다.
        // required = false >> 있어도 되고 없어도 된다, 필수 조건은 아니라는 것을 알려줌
        // 조회 조건으로 writer가 있어도 되고 없어도 된다. 필수는 아니다,
        // >> writer를 명시해주면 해당 writer가 작성한 일정만 보여주고, writer를 안넣어주면 전체조회를 하겠다.
        List<GetScheduleResponse> result = scheduleService.getAll(writer);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    //TODO @PathVariable vs @RequestParam
    // 1. @PathVariable >> /schedules/1 처럼 어떤 데이터를 딱 지칭할 때 주로 사용,
    // 2. @RequestParam >> key = value 형태의 쿼리스트링을 받을 때 주로 사용,
    // 데이터의 목록을 내가 원하는 조건으로 거르고 싶을 때 주로 사용함 (정렬, 필터링)

    //TODO 수정할 사용자의 id값을 URL 경로로 전달 받음
    @PutMapping("/schedules/{scheduleId}")
    //TODO 마찬가지로 ResponseEntity 응답용 클래스로 감싸주고,
    public ResponseEntity<UpdateScheduleResponse> updateSchedule(
            //TODO 수정할 사용자의 id는 @PathVariable로 전달을 받고,
            // 해당 사용자의 수정 내용은 @RequestBody로 전달 받음.
            @PathVariable Long scheduleId,
            @RequestBody UpdateScheduleRequest request)
    {
        //TODO scheduleService를 호출하여 update할 scheduleId값과 request를 매개변수로 넘겨주고,
        UpdateScheduleResponse result = scheduleService.update(scheduleId, request);
        //TODO Service애서 비즈니스 로직을 통해 DB에 갔다온 result라는 데이터값을
        // 클라이언트에게 상태코드와 같이 리턴해준다.
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    //TODO 클라이언트가 삭제할 사용자의 scheduleId값을 URL 경로로 넘겨줌
    // 그리고 이 값을 @PathVariable로 받아줌
    // GET요청 처럼 DELETE도 Body가 없음, 클라이언트가 URL 경로로 알려줌
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> delete(@PathVariable Long scheduleId) {
        scheduleService.delete(scheduleId);
        //TODO scheduleService.delete(scheduleId) >> 메서드를 호출하여 삭제하고,
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        //TODO 클라이언트에게 상태코드와 같이 응답을 내려줌
        // body(); vs build();
        // body(); >> 상자에 내용물을 채워서 보낼때 사용
        // build(); >> 상자에 담을 내용물은 없고, 상태코드만 만들어서 보낼때 사용
    }
}
