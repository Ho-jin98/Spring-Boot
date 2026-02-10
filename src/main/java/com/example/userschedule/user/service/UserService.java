package com.example.userschedule.user.service;
import com.example.userschedule.user.dto.*;
import com.example.userschedule.user.entity.User;
import com.example.userschedule.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public CreateUserResponse save(CreateUserRequest request){
        User user = new User(request.getUsername(),request.getEmail());
        User savedUser = userRepository.save(user);

        return new CreateUserResponse(
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
                () -> new IllegalStateException("없는 유저입니다.")
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
                () -> new IllegalStateException("없는 유저입니다.")
        );
        user.update(request.getUsername(), request.getEmail());
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
            throw new IllegalStateException("없는 유저입니다.");
        }
        userRepository.deleteById(userId);
    }
}
