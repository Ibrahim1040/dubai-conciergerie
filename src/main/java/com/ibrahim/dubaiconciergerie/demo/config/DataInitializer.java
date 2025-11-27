package com.ibrahim.dubaiconciergerie.demo.config;

import com.ibrahim.dubaiconciergerie.demo.entity.User;
import com.ibrahim.dubaiconciergerie.demo.entity.User.Role;
import com.ibrahim.dubaiconciergerie.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                User admin = new User();
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setFirstName("Admin");
                admin.setLastName("System");
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println("=== ADMIN CREATED ===");
            } else {
                System.out.println("=== ADMIN ALREADY EXISTS ===");
            }
        };
    }
}
