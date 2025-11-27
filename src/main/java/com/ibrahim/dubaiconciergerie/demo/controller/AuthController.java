package com.ibrahim.dubaiconciergerie.demo.controller;

import com.ibrahim.dubaiconciergerie.demo.security.JwtUtil;
import com.ibrahim.dubaiconciergerie.demo.entity.User;
import com.ibrahim.dubaiconciergerie.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponse(token, user.getEmail(), user.getRole().name());
    }

    @PostMapping("/admin/fix-all-passwords")
    public String fixAllPasswords() {
        List<User> users = userRepository.findAll();

        for (User u : users) {
            // Si déjà BCrypt, on ne touche pas
            if (u.getPassword() != null && u.getPassword().startsWith("$2a$")) {
                continue;
            }

            // Sinon on encode un nouveau mot de passe par défaut
            String defaultPass = "ChangeMe123!";
            u.setPassword(passwordEncoder.encode(defaultPass));
            userRepository.save(u);
        }

        return "All passwords updated. Default password = ChangeMe123!";
    }

}
