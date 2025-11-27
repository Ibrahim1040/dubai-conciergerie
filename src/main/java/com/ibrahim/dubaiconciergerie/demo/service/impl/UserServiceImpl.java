package com.ibrahim.dubaiconciergerie.demo.service.impl;

import com.ibrahim.dubaiconciergerie.demo.dto.UserDto;
import com.ibrahim.dubaiconciergerie.demo.dto.UserMapper;
import com.ibrahim.dubaiconciergerie.demo.entity.User;
import com.ibrahim.dubaiconciergerie.demo.repository.UserRepository;
import com.ibrahim.dubaiconciergerie.demo.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ========= LISTER =========
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    // ========= GET BY ID =========
    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
    }

    // ========= CREER =========
    @Override
    public User create(UserDto dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        User user = UserMapper.fromDto(dto);

        // Encoder le mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // ========= UPDATE =========
    @Override
    public User update(Long id, UserDto dto) {

        User existing = getById(id);

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());

        // email modifié ? vérification
        if (!existing.getEmail().equals(dto.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("Email déjà utilisé");
            }
            existing.setEmail(dto.getEmail());
        }

        // update role
        existing.setRole(User.Role.valueOf(dto.getRole()));

        // update password seulement si présent
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return userRepository.save(existing);
    }

    // ========= DELETE =========
    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Utilisateur introuvable");
        }
        userRepository.deleteById(id);
    }
}
