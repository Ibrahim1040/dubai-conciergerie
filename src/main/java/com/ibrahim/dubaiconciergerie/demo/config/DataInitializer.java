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
    CommandLineRunner init(UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {

            if (userRepo.findByEmail("superadmin@example.com").isEmpty()) {
                User admin = User.builder()
                        .firstName("Super")
                        .lastName("Admin")
                        .email("superadmin@example.com")
                        .password(encoder.encode("Admin2024")) // HASH OBLIGATOIRE
                        .role(User.Role.ADMIN)
                        .build();

                userRepo.save(admin);

                System.out.println("🔥 ADMIN created: superadmin@example.com / Admin2024");
            }
            if (userRepo.findByEmail("admin@example.com").isEmpty()) {
                User admin = User.builder()
                        .firstName("Admin")
                        .lastName("System")
                        .email("admin@example.com")
                        .password(encoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .build();
                userRepo.save(admin);

                System.out.println("🔥 ADMIN created: admin@example.com / admin123");
            }

        };
    }

}
