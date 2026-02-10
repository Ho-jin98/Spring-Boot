package com.example.userschedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UserScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserScheduleApplication.class, args);
    }

}
