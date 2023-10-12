package com.abdourahmane.spring_security.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.abdourahmane.spring_security.entity.User;
import com.abdourahmane.spring_security.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UserConfig {
     private final UserRepository userRepository;
    @Bean
    public CommandLineRunner commandLineRunner(){
        return args->{
            User bass = new User(
                "Diallo",
                "Abdoul",
                "Abdoul@gmail.com",
                "Abdoul@gmail.com",
                passwordEncoder().encode("passer"),
                "ADMIN"
            );
            // userRepository.save(bass);
        };
    }


    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
