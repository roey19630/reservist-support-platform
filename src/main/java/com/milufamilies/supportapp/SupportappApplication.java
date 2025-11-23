package com.milufamilies.supportapp;

import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.model.enums.Role;
import com.milufamilies.supportapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SupportappApplication {

    public static    void main(String[] args) {
        SpringApplication.run(SupportappApplication.class, args);
    }

    @Bean
    CommandLineRunner createAdmin(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (!userRepository.existsByRole(Role.ROLE_ADMIN)) {
                User admin = User.builder()
                        .fullName("Admin Admin")
                        .email("admin@admin.com")
                        .idNumber("0000000001")
                        .password(encoder.encode("admin123"))
                        .phone("0500000000")
                        .role(Role.ROLE_ADMIN)
                        .approved(true)
                        .build();

                userRepository.save(admin);
                System.out.println("✅ Admin created successfully: admin@admin.com / admin123");
            } else {
                System.out.println("ℹ Admin already exists. Skipping creation.");
            }
        };
    }

}
