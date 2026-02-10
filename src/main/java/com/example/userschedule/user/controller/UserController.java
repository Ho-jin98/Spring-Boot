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
    private  final UserService userService;

    //TODO 회원가입, 유저 등록 = 회원가입이니까!
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(request));
    }
    //TODO 로그인
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request, HttpSession session){
        SessionUser sessionUser = userService.login(request);
        session.setAttribute("loginUser", sessionUser);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    //TODO 로그 아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
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
    public ResponseEntity<UpdateUserResponse> updateUser(
            @PathVariable Long userId, @RequestBody UpdateUserRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(userId, request));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
