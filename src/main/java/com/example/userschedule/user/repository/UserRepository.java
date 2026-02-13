package com.example.userschedule.user.repository;

import com.example.userschedule.user.entity.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsernameOrderByModifiedAtDesc(String username);
    List<User> findByOrderByModifiedAtDesc();
    Optional<User> findByEmail(@Email String email);
}
