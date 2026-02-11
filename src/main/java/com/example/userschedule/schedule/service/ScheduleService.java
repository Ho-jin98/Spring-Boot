package com.example.userschedule.schedule.service;

import com.example.userschedule.exception.ScheduleNotFoundException;
import com.example.userschedule.exception.UserNotFoundException;
import com.example.userschedule.schedule.dto.*;
import com.example.userschedule.schedule.entity.Schedule;
import com.example.userschedule.schedule.repository.ScheduleRepository;
import com.example.userschedule.user.entity.User;
import com.example.userschedule.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateScheduleResponse save(Long userId, CreateScheduleRequest request){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("존재하지 않는 유저입니다.")
        );

        Schedule schedule = new Schedule(request.getWriter(),request.getTitle(),request.getContent(), user);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new CreateScheduleResponse(
                savedSchedule.getId(),
                savedSchedule.getWriter(),
                savedSchedule.getTitle(),
                savedSchedule.getContent(),
                savedSchedule.getCreatedAt(),
                savedSchedule.getModifiedAt()
        );
    }

    @Transactional(readOnly = true)
    public GetScheduleResponse getSchedule(Long scheduleId){
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new ScheduleNotFoundException("조회되는 일정이 없습니다.")
        );
        return new GetScheduleResponse(
                schedule.getId(),
                schedule.getWriter(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<GetScheduleResponse> getSchedules(Long userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("존재하지 않는 유저입니다.")
        );
        List <Schedule> schedules = scheduleRepository.findByUser(user);
        return schedules.stream()
                .map(schedule -> new GetScheduleResponse(
                        schedule.getId(),
                        schedule.getWriter(),
                        schedule.getTitle(),
                        schedule.getContent(),
                        schedule.getCreatedAt(),
                        schedule.getModifiedAt()
                )).toList();
    }

    @Transactional
    public UpdateScheduleResponse update(Long scheduleId, UpdateScheduleRequest request){
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new ScheduleNotFoundException("수정할 일정이 없습니다.")
        );
        schedule.update(request.getWriter(), request.getTitle(), request.getContent());
        return new UpdateScheduleResponse(
                schedule.getId(),
                schedule.getWriter(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    @Transactional
    public void delete(Long scheduleId){
        boolean existence = scheduleRepository.existsById(scheduleId);
        if(!existence){
            throw new ScheduleNotFoundException("삭제할 일정이 없습니다.");
        }
        scheduleRepository.deleteById(scheduleId);
    }
}
