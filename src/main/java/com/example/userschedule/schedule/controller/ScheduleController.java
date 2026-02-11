package com.example.userschedule.schedule.controller;

import com.example.userschedule.common.CommonResponse;
import com.example.userschedule.schedule.dto.*;
import com.example.userschedule.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("/users/{userId}/schedules")
    public ResponseEntity<CommonResponse<CreateScheduleResponse>> createSchedule(
            @PathVariable Long userId,
            @Valid @RequestBody CreateScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(HttpStatus.CREATED, "일정 생성 성공",
                scheduleService.save(userId, request)));
    }

    @GetMapping("/users/{userId}/schedules")
    public ResponseEntity<List<GetScheduleResponse>> getSchedules(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getSchedules(userId));
    }

    //TODO @Positive >> userId가 항상 양수여야한다는 규칙
    // 클래스 레벨에 @Validated가 선언되어 있어야지 @Positive 어노테이션이 동작할 수 있다.
    // @Valid와 @Positive는 감시 영역이 다름,
    // @Valid -> @RequestBody Request: "이 봉투(Request) 안에 든 내용물들을 검사해줘!"
    // @PathVariable -> @Positive Long id: "이 숫자(id) 자체가 양수인지 검사해줘!"

    @GetMapping("/users/{userId}/schedules/{scheduleId}")
    public ResponseEntity<GetScheduleResponse> getSchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getSchedule(scheduleId));
    }

    @PutMapping("/users/{userId}/schedules/{scheduleId}")
    public ResponseEntity<CommonResponse<UpdateScheduleResponse>> updateSchedule(
            @PathVariable Long scheduleId, @Valid @RequestBody UpdateScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(HttpStatus.OK, "변경 완료",
                scheduleService.update(scheduleId, request)));
    }

    @DeleteMapping("/users/{userId}/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.delete(scheduleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

