package com.logicea.cards.config;

import com.logicea.cards.user.Role;
import com.logicea.cards.user.User;
import com.logicea.cards.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

/**
 * @author Alex Kiburu
 */
@Configuration
@Slf4j
public class InitializeData {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        return args -> {
            // add admin if doesn't exist
            Optional<User> adminOptional = userRepository.findByEmail("admin@example.com");
            if(adminOptional.isEmpty()){
                User admin = User.builder()
                        .firstname("admin")
                        .lastname("admin")
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .build();
                userRepository.save(admin);
                log.info("Preloading admin user: admin@example.com");
            }

            // add user if doesn't exist
            Optional<User> userOptional = userRepository.findByEmail("user@example.com");
            if(userOptional.isEmpty()){
                User user = User.builder()
                        .firstname("user")
                        .lastname("user")
                        .email("user@example.com")
                        .password(passwordEncoder.encode("user123"))
                        .role(Role.USER)
                        .build();
                userRepository.save(user);
                log.info("Preloading user: user@example.com");
            }
        };
    }
}
