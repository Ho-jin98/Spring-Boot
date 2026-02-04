package com.example.schedule.service;

import com.example.schedule.dto.*;
import com.example.schedule.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.schedule.repository.ScheduleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    //TODO Service는 Repository의 상위 레이어 이므로 Repository를 호출
    private final ScheduleRepository scheduleRepository;

    //TODO Transactional >> 원자성
    @Transactional
    //TODO Entity는 JAP(DB)에 데이터를 저장할 때 사용하는 객체여서 Entity는 거의 DB 원본과 유사하다고 볼 수 있는데,
    // 이러한 Entity를 클라이언트에게 직접 전달할 수는 없으니, DTO라는 데이터 전달 객체 담아 만들어서 보여준다.
    // 그래서 CreateScheduleResponse DTO를 만들었으니, save메서드를 DTO에 담아주기 위한 작업이고,
    // 매개변수로 클라이언트가 보내준 데이터 CreateScheduleRequest를 받아준다.
    // 그런데 주의할 점은 Schedule 객체를 생성한 것은 Entity에 있는 객체이다.
    // Repository와 DB의 다리를 연결해주는 객체가 Entity니까 Service는 DB원본과 유사한 Entity 객체의 정보를 활용한다.
    public CreateScheduleResponse save(CreateScheduleRequest request) {
        //TODO 밑에 부분은 클라이언트가 보내준 주문서(DTO)를 보고, 정식 양식(Entity)을 새로 만드는 과정
        // Entity의 생성자를 호출하고, schedule 참조변수에 요청 필드들을 담아주는 작업
        // request.get, getter로 접근한 데이터들은 현재 request라는 DTO 객체 안에 머물고 있는 상태,
        // 하지만 밑에 있는 Schedule savedSchedule = scheduleRepository.save(schedule);을 보면
        // Schedule(Entity) 타입만 받을 수 있게 설계되어 있으므로 request DTO에 있는 데이터들을
        // Entity라는 새 바구니로 옮겨 담아주는 작업이다.
        Schedule schedule = new Schedule(
                request.getTitle(),
                request.getContents(),
                request.getWriter(),
                request.getPassword()
        );
        //TODO savedSchedule은 요청 DTO에 있는 데이터들을 Entity 객체의 참조변수 schedule로 옮겨줬으니,
        // 이 데이터들을 Repository로 저장하려고 scheduleRepository를 호출해주고,
        // save 매서드 매개변수에 schedule을 담아서 데이터를 DB에 저장하는 작업
        Schedule savedSchedule = scheduleRepository.save(schedule);

        //TODO 여기서 흥미로운 사실, schedule vs savedSchedule
        // schedule >> 저장 전, DB를 갔다 오기 전, 저장 대기용 객체 (id값이 null)
        // schedule >> 비영속 상태, 순수 자바 객체
        // savedSchedule >> 저장 후, DB를 갔다 온 상태, DB에 기록된 후 내용이 채워져서 돌아온 확정본 객체 (id값 생성O)
        // savedSchedule >> 영속 상태, JPA가 관리하기 시작
        /**
         CreateScheduleResponse response = new CreateScheduleResponse(
         savedSchedule.getId(),
         savedSchedule.getTitle(),
         savedSchedule.getContents(),
         savedSchedule.getUsername(),
         savedSchedule.getPassword()
         );
         */
        //TODO 여기서의 return은 Service보다 상위 레이어인 Controller에게 보내주는 리턴,
        // 위에 과정을 통해 request DTO에 있는 데이터들을 schedule 참조변수에 옮기고,
        // 그 데이터들이 담겨있는 schedule을 scheduleRepository를 호출하여 save메서드의 매개변수에 담아
        // savedSchedule 변수로 저장을 해줬으니, 이 영속된 객체에서 필요한 정보를 getter를 통해 꺼내어
        // 다시 CreateScheduleResponse라는 응답용 DTO에 담아서 상위 레이어 Controller에게 리턴해주는 작업
        return new CreateScheduleResponse(
                savedSchedule.getId(),
                savedSchedule.getTitle(),
                savedSchedule.getContents(),
                savedSchedule.getWriter(),
                savedSchedule.getCreatedAt(),
                savedSchedule.getModifiedAt()
        );
    }
    // 단 건 조회
    @Transactional(readOnly = true)//TODO readOnly = true >> 읽기 전용, 성능 최적화
    //TODO 단 건을 조회할 때는 어떤 사용자를 조회할지 Id값이 필요함 (Controller에서 받아 온 scheduleId)
    public GetScheduleResponse getOne(Long scheduleId){
        //TODO findBy >> By뒤에는 조건 필드가옴, 여기서는 Id가 조건 필드값이고, scheduleId는 클라이언트가 요청한 사용자인데,
        // 매개변수로 받아서 해당 id를 조회하겠다는 의미
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                //TODO orElseThrow > 조회결과가 없을 수도 있으니 그에 대비하는 예외를 던져주는 것.
                // Optional을 벗겨내는 과정
                () -> new IllegalStateException("없는 일정 입니다.")
        );
        //TODO GetScheduleResponse를 리턴해줄 건데, scheduleRepository 메서드를 호출하고
        // findById(scheduleId) >> 요청된 사용자의 아이디를 찾겠다, 그리고 만약 없으면 oeElseThrow 예외를 던져줘라
        // findById가 orElseThrow를 통과하는 순간 schedule이라는 변수 안에는 DB에서 긁어온 해당 id의 모든 정보가
        // 가득 차있는 상태가 된다. 그리고 이것을 getter를 활용하여 필요한 필드만 가져온다.
        return new GetScheduleResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContents(),
                schedule.getWriter(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
                //TODO schedule안에 있는 필요한 필드값들을 getter를 활용하여 가져옴
        );
    }
    // 여러개 조회
    @Transactional(readOnly = true) //TODO readOnly = true >> 읽기 전용, 성능 최적화
    //TODO List를 사용하는 이유, 여러개를 조회한다는 것은 결과가 1개일 수도, 100개일 수도 아니면 0개일 수도 있다는 뜻,
    // 자바의 규칙상 하나의 변수에는 하나의 객체만 담을 수 있다. 그래서 List를 사용해주는 것.
    // 그리고 만약 조회결과가 없다면 List는 빈 배열을 반환하기 때문에 에러방지도 된다.
    public List<GetScheduleResponse> getAll(String writer){
        List<Schedule> schedules;
        if(writer != null) {
            //TODO findAllBy >> By뒤에는 조건 필드가옴, 조건 = Writer
            // OrderBy뒤에는 정렬 기준이옴, ModifiedAt = 수정일을 기준으로 하겠다
            // Desc >> 내림차순을 의미
            schedules = scheduleRepository.findAllByWriterOrderByModifiedAtDesc(writer);
        } else{
            //TODO findAllByOrderBy >> By와 OrderBy사이에 필드값이 없으므로, 조건 필드가 없음
            // OrderBy뒤에는 정렬 기준이옴, ModifiedAt = 수정일을 기준으로 하겠다
            // Desc >> 내림차순을 의미
            schedules = scheduleRepository.findAllByOrderByModifiedAtDesc();
        }

        //TODO 리턴 해줄 때는 GetScheduleResponse DTO로 리턴을 해줄건데,
        // 지금 조회 결과를 Entity의 schedule List 안에 저장을 해놔서 Entity를 직접 전달할 수 없으니,
        // 리턴 타입을 맞춰주기 위해 for문을 이용하여 List<Schedule> schedules 안에 있는 데이터를
        // GetScheduleResponse DTO List로 옮기고 Controller에게 넘겨주기 위한 작업
        List<GetScheduleResponse> dtos = new ArrayList<>();
        //TODO dtos라는 GetScheduleResponse 타입의 ArrayList 생성만 한 것, 여기까진 비어있는 상태
        for (Schedule schedule : schedules) {
            //TODO 위에서 만들었던, Entity의 List schedules안에 있는 데이터를 for문을 이용하여 schedule 변수로 옮겨준다.
            // for문으로 데이터를 schedule로 옮기긴 했지만, 아직까지 타입은 Schedule임, List에 들어가지 않은 상태
            GetScheduleResponse dto = new GetScheduleResponse(
                    //TODO 최종 반환해줄 타입은 GetScheduleResponse인데, 아직 schedule의 타입은 Schedule이므로,
                    // GetScheduleResponse의 생성자를 호출하여 dto라는 GetScheduleResponse 타입의 새로운 참조 변수를 만들어주고,
                    // getter로 schedule안에 있는 필요한 데이터들을 하나씩 가져온다.
                    schedule.getId(),
                    schedule.getTitle(),
                    schedule.getContents(),
                    schedule.getWriter(),
                    schedule.getCreatedAt(),
                    schedule.getModifiedAt()
                    //TODO getter로 하나씩 꺼내오는 이유는, 만약 schedule(Entity)객체에서 필요한 것만 가져와서 클라이언트에게
                    // 전달하는 것이 아니고, schedule(Entity)객체를 통째로 준다면, 민감한 필드까지 모두 노출될 것이다.
            );
            dtos.add(dto);
        }
        return dtos;
    }

    @Transactional
    //TODO Controller에서 전달해준 scheduleId와 request를 매개변수로 받아준다.
    public UpdateScheduleResponse update(Long scheduleId, UpdateScheduleRequest request) {
        //TODO scheduleRepository를 호출하고, findById >> 찾을 사용자의 scheduleId값을 넘겨준다.
        // 만약 없다면 orElseThrow로 예외를 던져준다.
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("없는 일정 입니다.")
        );
        //TODO findById 가 orElseThrow를 통과하는 순간 해당 사용자(scheduleId)의 데이터가 schedule 변수에 가득 참
        // title과 writer만 수정 가능하다는 조건이 있었으므로, request DTO에 getter로 접근하여 수정할 필드값을 꺼내주고,
        // 이 값들을 schedule(Entity)에 있는 update메서드에 넣어준다.
        schedule.update(
                request.getTitle(),
                request.getWriter()
                //TODO schedule(Entity)의 데이터를 request(DTO)에 있는 데이터로 갈아 끼우는 중
        );
        //TODO return할 때는 타입을 UpdateScheduleResponse로 맞춰주고,
        // 응답 DTO에 있는 생성자의 매개변수대로 필드를 채워준다. 응답을 내려줄 때는 완성된 전체의 모습을 보여주는게 마땅함
        // 그치만 title과 writer만 수정 가능했으므로, 이 두개의 필드값만 변경이 되어있고, 나머진 그대로 유지
        return new UpdateScheduleResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContents(),
                schedule.getWriter(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    @Transactional
    public void delete(Long scheduleId){
        //TODO existence >> 지역변수, 알아보기 쉬운 이름으로 지은 것.
        boolean existence = scheduleRepository.existsById(scheduleId);
        //TODO existsById >> JPA의 약속된 메서드
        // exists + By + Id >> 존재하니? + ~을 기준으로 + Pk값
        //TODO 삭제할 게시물이 없는 경우
        if(!existence){
            throw new IllegalStateException("없는 일정입니다.");
        }
        //TODO 삭제할 게시물이 있는 경우
        scheduleRepository.deleteById(scheduleId);

        //TODO findById vs existsById
        // findById >> 데이터 전체를 다 긁어와서 상자에 담아옴
        // existsById >> 데이터가 있는지 없는지만 보고 오는 것
        // 그래서 수정할 때는 데이터를 가져와서 내용을 바꿔야하니, findById를 사용하고,
        // 삭제할 때는 어차피 내용을 지울거라 있는지 내용은 궁금하지 않고, 없는지만 확인하면 된다.
    }

}
