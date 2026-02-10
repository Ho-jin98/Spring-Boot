package com.example.userschedule.user.repository;

import com.example.userschedule.schedule.entity.Schedule;
import com.example.userschedule.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsernameOrderByModifiedAtDesc(String username);
    List<User> findByOrderByModifiedAtDesc();
}
