package com.example.userschedule.user.service;
import com.example.userschedule.exception.EmailNotFoundException;
import com.example.userschedule.exception.PasswordDoesNotMatch;
import com.example.userschedule.exception.UserNotFoundException;
import com.example.userschedule.user.dto.*;
import com.example.userschedule.user.entity.User;
import com.example.userschedule.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public SignupResponse save(SignupRequest request){
        User user = new User(request.getUsername(),request.getEmail(),request.getPassword());
        User savedUser = userRepository.save(user);

        return new SignupResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getCreatedAt(),
                savedUser.getModifiedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<GetUserResponse> getUsers(String username){
        List<User> users;
        if(username != null){
            users = userRepository.findByUsernameOrderByModifiedAtDesc(username);
        }else{
            users = userRepository.findByOrderByModifiedAtDesc();
        }
        return users.stream()
                .map(user -> new GetUserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getCreatedAt(),
                        user.getModifiedAt()
                )).toList();
    }

    @Transactional(readOnly = true)
    public GetUserResponse getUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("존재하지 않는 유저입니다.")
        );
        return new GetUserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }

    @Transactional
    public UpdateUserResponse update(Long userId, UpdateUserRequest request){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("존재하지 않는 유저입니다.")
        );
        user.update(request.getUsername());
        return new UpdateUserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }

    @Transactional
    public void delete(Long userId){
        boolean existence = userRepository.existsById(userId);
        if (!existence){
            throw new UserNotFoundException("존재하지 않는 유저입니다.");
        }
        userRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    //TODO @Valid >> 요청받은 로그인 정보와 일치하는지 검증
    // 현재 요청DTO에는 email, password가 있음.
    public SessionUser login(@Valid LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new EmailNotFoundException("잘못된 이메일 입니다.")
        );
        //TODO 회원가입할 때의 비밀번호와, 요청받은 비밀번호가 일치하는지 확인
        // 하지만 여기까지만 해놓으면 500에러로 표시가되기 때문에 커스텀 에러로 진행
        if(!user.getPassword().equals(request.getPassword())){
            throw new PasswordDoesNotMatch("비밀번호가 일치하지 않습니다.");
        }
        return new SessionUser(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
