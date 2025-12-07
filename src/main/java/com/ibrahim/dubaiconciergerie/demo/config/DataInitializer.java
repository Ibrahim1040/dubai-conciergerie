package com.ibrahim.dubaiconciergerie.demo.config;

import com.ibrahim.dubaiconciergerie.demo.entity.User;
import com.ibrahim.dubaiconciergerie.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.findByEmail("ibrahim@gmail.com").isEmpty()) {
            User owner = User.builder()
                    .firstName("Ibrahim")
                    .lastName("Aharrar")
                    .email("ibrahim@gmail.com")
                    .password(passwordEncoder.encode("Admin2024"))
                    .role(User.Role.OWNER)
                    .build();

            userRepository.save(owner);
        }

        // tes autres users ADMIN si tu veux
    }
}
