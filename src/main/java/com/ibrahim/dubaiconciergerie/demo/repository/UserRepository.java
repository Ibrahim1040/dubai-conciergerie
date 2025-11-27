package com.ibrahim.dubaiconciergerie.demo.repository;

import com.ibrahim.dubaiconciergerie.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    // 👉 Méthode pour vérifier si un email existe (Spring Data l'implémente automatiquement)
    boolean existsByEmail(String email);
}



