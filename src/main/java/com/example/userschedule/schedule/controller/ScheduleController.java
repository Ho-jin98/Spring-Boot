package com.example.userschedule.schedule.controller;

import com.example.userschedule.schedule.dto.*;
import com.example.userschedule.schedule.service.ScheduleService;
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
    public ResponseEntity<CreateScheduleResponse> createSchedule(
            @PathVariable Long userId,
            @RequestBody CreateScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.save(userId, request));
    }

    @GetMapping("/users/{userId}/schedules")
    public ResponseEntity<List<GetScheduleResponse>> getSchedules(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getSchedules(userId));
    }

    @GetMapping("/users/{userId}/schedules/{scheduleId}")
    public ResponseEntity<GetScheduleResponse> getSchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getSchedule(scheduleId));
    }

    @PutMapping("/users/{userId}/schedules/{scheduleId}")
    public ResponseEntity<UpdateScheduleResponse> updateSchedule(
            @PathVariable Long scheduleId, @RequestBody UpdateScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.update(scheduleId, request));
    }

    @DeleteMapping("/users/{userId}/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.delete(scheduleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

