package com.example.userschedule.user.service;
import com.example.userschedule.config.PasswordEncoder;
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
    //TODO PasswordEncoder 클래스에 @Getter를 달아줘서 호출
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponse save(SignupRequest request){
        //TODO 핵심은 user에 비밀번호를 담기 전에!! 암호화를 해줘야함,
        // 암호화가 안된상태로 user변수에 담기면 그대로 DB에 저장되므로 위험!! 순서가 매우 중요하다.
        // encode(request.getPassword())으로 요청 DTO에 있는 비밀번호 필드를 가져와서 encodedPassword에 담아준다.
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        //TODO 자연스럽게 encodedPassword를 쓰윽 넣어준다.
        User user = new User(request.getUsername(),request.getEmail(),encodedPassword);
        User savedUser = userRepository.save(user);

        return new SignupResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getCreatedAt(),
                savedUser.getModifiedAt()
        );
    }

    //TODO @Valid >> 요청받은 로그인 정보와 일치하는지 검증
    // 현재 요청DTO에는 email, password가 있음.

    @Transactional(readOnly = true)
    public SessionUser login(@Valid LoginRequest request) {
        //TODO 먼저 userRepository에서 요청을 보낸 Email이 맞는지 확인해서 찾아와줌,
        // 없으면 에러를 던져준다.
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new EmailNotFoundException("잘못된 이메일 입니다.")
        );
        //TODO 회원가입할 때의 비밀번호와, 요청받은 비밀번호가 일치하는지 확인
        // 하지만 여기까지만 해놓으면 500에러로 표시가되기 때문에 커스텀 에러로 진행

        //TODO matches를 호출해서 생성자대로 만들어준다. 요청을 보낸 비밀번호와 DB에 암호화되어 있는 비밀번호가 맞는지 비교
        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!matches){
            throw new PasswordDoesNotMatch("비밀번호가 일치하지 않습니다.");
        }
        return new SessionUser(
                user.getId(),
                user.getUsername(),
                user.getEmail()
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
}
