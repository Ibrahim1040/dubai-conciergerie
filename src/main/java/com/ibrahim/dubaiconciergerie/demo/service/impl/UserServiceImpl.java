package com.ibrahim.dubaiconciergerie.demo.service.impl;

import com.ibrahim.dubaiconciergerie.demo.dto.UserDto;
import com.ibrahim.dubaiconciergerie.demo.dto.UserMapper;
import com.ibrahim.dubaiconciergerie.demo.entity.User;
import com.ibrahim.dubaiconciergerie.demo.exception.ResourceNotFoundException;
import com.ibrahim.dubaiconciergerie.demo.repository.UserRepository;
import com.ibrahim.dubaiconciergerie.demo.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(UserDto dto) {
        User user = UserMapper.fromDto(dto);
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id " + id));
    }

    @Override
    public User update(Long id, UserDto dto) {
        User existing = getById(id);

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());

        if (dto.getPassword() != null) {
            existing.setPassword(dto.getPassword());
        }

        if (dto.getRole() != null) {
            existing.setRole(User.Role.valueOf(dto.getRole()));  // 🔥 ici la conversion String -> enum
        }

        return userRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = getById(id);

        // Si c’est un owner et qu’il a encore des propriétés, on bloque
        if (user.getProperties() != null && !user.getProperties().isEmpty()) {
            throw new IllegalStateException(
                    "Impossible de supprimer cet utilisateur : il possède encore des propriétés. " +
                            "Veuillez d'abord supprimer ou réassigner ses biens."
            );
        }

        userRepository.delete(user);
    }
}
