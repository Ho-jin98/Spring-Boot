package com.example.userschedule.comment.service;

import com.example.userschedule.comment.dto.*;
import com.example.userschedule.comment.entity.Comment;
import com.example.userschedule.comment.repository.CommentRepository;
import com.example.userschedule.exception.CommentNotFoundException;
import com.example.userschedule.exception.ForbiddenException;
import com.example.userschedule.exception.ScheduleNotFoundException;
import com.example.userschedule.exception.UserNotFoundException;
import com.example.userschedule.schedule.entity.Schedule;
import com.example.userschedule.schedule.repository.ScheduleRepository;
import com.example.userschedule.user.entity.User;
import com.example.userschedule.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CreateCommentResponse save(Long userId, Long scheduleId, @Valid CreateCommentRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("존재하지 않는 유저입니다.")
        );
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new ScheduleNotFoundException("없는 일정입니다.")
        );
        Comment comment = new Comment(request.getReply(), user, schedule);
        Comment savedComment = commentRepository.save(comment);

        return new CreateCommentResponse(
                savedComment.getId(),
                savedComment.getReply(),
                savedComment.getCreatedAt(),
                savedComment.getModifiedAt()
        );
    }

    @Transactional(readOnly = true)
    public GetCommentResponse getComment(Long scheduleId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException("없는 댓글 입니다.")
        );
        if(comment.isDeleted()){
            throw new CommentNotFoundException("이미 삭제된 댓글입니다.");
        }
        if(!comment.getSchedule().getId().equals(scheduleId)){
            throw new CommentNotFoundException("해당 일정에 없는 댓글입니다.");
        }
        return new GetCommentResponse(
                comment.getId(),
                comment.getReply(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }
    @Transactional(readOnly = true)
    public List<GetCommentResponse> getComments(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new ScheduleNotFoundException("없는 일정입니다.")
        );
        List<Comment> comments = commentRepository.findAllByScheduleIdAndIsDeletedFalse(scheduleId);

        if(comments.isEmpty()){
            throw new CommentNotFoundException("댓글이 존재하지 않습니다.");
        }

        return comments.stream()
                .map(comment -> new GetCommentResponse(
                        comment.getId(),
                        comment.getReply(),
                        comment.getCreatedAt(),
                        comment.getModifiedAt()
                )).toList();
    }

    @Transactional
    public UpdateCommentResponse update(Long commentId, Long userId, Long scheduleId, @Valid UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException("없는 댓글입니다.")
        );
        if(!comment.getUser().getId().equals(userId)) {
            throw new UserNotFoundException("잘못된 유저입니다.");
        }
        if(!comment.getSchedule().getId().equals(scheduleId)) {
            throw new ScheduleNotFoundException("없는 일정입니다");
        }

        comment.update(request.getReply());
        return  new UpdateCommentResponse(
                comment.getId(),
                comment.getReply(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }

    @Transactional
    public void delete(Long commentId, Long userId, Long scheduleId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException("없는 댓글입니다.")
        );
        if(!comment.getUser().getId().equals(userId)){
            throw new ForbiddenException("삭제 권한이 없습니다.");
        }
        if(!comment.getSchedule().getId().equals(scheduleId)) {
            throw new ScheduleNotFoundException("해당 일정의 댓글이 아닙니다.");
        }
        //TODO commentRepository.delete(comment); << 이렇게 사용하면 DB에서 완전히 삭제시키는것이므로,
        // 나중에 복구하고 싶어도 불가능,
        comment.deleted(true);
        //TODO 현재 Comment 엔티티에 isDelet = false 필드를 두고, deleted 메서드를 만들었으므로,
        // deleted 메서드를 호출하여 지우게되면 DB에는 정보가 남아있는데 isDeleted가 true가 됨
        // 나중에 복구가 가능하고, 삭제되었다고 메세지로 전달해주면된다.
    }
}
