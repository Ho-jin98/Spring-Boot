package com.example.userschedule.user.controller;

import com.example.userschedule.user.dto.*;
import com.example.userschedule.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private  final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(request));
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
