package com.ibrahim.dubaiconciergerie.demo.controller;

import com.ibrahim.dubaiconciergerie.demo.dto.UserDto;
import com.ibrahim.dubaiconciergerie.demo.dto.UserMapper;
import com.ibrahim.dubaiconciergerie.demo.entity.User;
import com.ibrahim.dubaiconciergerie.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@Tag(name = "Users", description = "Endpoints d'administration des utilisateurs")
@CrossOrigin // si tu appelles depuis le front
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1) Lister tous les utilisateurs
    @GetMapping
    @Operation(summary = "Lister tous les utilisateurs")
    public List<UserDto> getAllUsers() {
        List<User> users = userService.getAll();
        return users.stream()
                .map(UserMapper::toDto)
                .toList();
    }

    // 2) Récupérer un utilisateur par id
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un utilisateur par id")
    public UserDto getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        return UserMapper.toDto(user);
    }

    // 3) Créer un utilisateur (OWNER ou ADMIN)
    @PostMapping
    @Operation(summary = "Créer un nouvel utilisateur")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto dto) {
        User created = userService.create(dto);
        UserDto response = UserMapper.toDto(created);

        // Optionnel : Location header
        return ResponseEntity
                .created(URI.create("/api/admin/users/" + created.getId()))
                .body(response);
    }

    // 4) Mettre à jour un utilisateur
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un utilisateur")
    public UserDto updateUser(@PathVariable Long id,
                              @Valid @RequestBody UserDto dto) {
        User updated = userService.update(id, dto);
        return UserMapper.toDto(updated);
    }

    // 5) Supprimer un utilisateur
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un utilisateur")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

