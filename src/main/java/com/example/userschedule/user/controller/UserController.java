package com.example.userschedule.user.controller;

import com.example.userschedule.user.dto.*;
import com.example.userschedule.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //TODO 회원가입, 유저 등록 = 회원가입이니까!
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(request));
    }
    //TODO 로그인
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request, HttpSession session){
        //TODO SessionUser DTO에 로그인 정보를 담아 저장해주기 위해 userService를 호출해주고, login 메서드에
        // 요청 DTO를 담아서 저장해준다.
        SessionUser sessionUser = userService.login(request);
        session.setAttribute("loginUser", sessionUser);
        //TODO setAttribute >> 세션이 저장되어 있는 메모리에 로그인 정보를 담아주는 기능이라고 생각하면 되는데,
        // "loginUser", sessionUser -> Key-Value 형태로 로그인 정보를 저장해준다.
        return ResponseEntity.status(HttpStatus.OK).build();
        //TODO SessionUser DTO 이름은 자유롭게 바꿀 수 있고, sessionUser 변수명도 내가 원하는 변수명으로 설정할 수 있다.
    }
    //TODO 로그 아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            //TODO @SessionAttribute >> 세션에 저장되어 있는 정보를 불러와주는 어노테이션
            // getAttribute와 비슷한 기능이지만, 어노테이션이므로 파라미터에 사용
            @SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser, HttpSession session
    ){
        if(sessionUser == null){
            return ResponseEntity.badRequest().build();
        }
        session.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<GetUserResponse>> findUsers(String username){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers(username));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<GetUserResponse> findUser(@PathVariable Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(userId));
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UpdateUserResponse> updateUserName(
            @PathVariable Long userId, @RequestBody UpdateUserRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(userId, request));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
