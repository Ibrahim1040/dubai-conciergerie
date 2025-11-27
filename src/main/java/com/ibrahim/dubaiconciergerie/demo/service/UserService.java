package com.ibrahim.dubaiconciergerie.demo.service;

import com.ibrahim.dubaiconciergerie.demo.dto.UserDto;
import com.ibrahim.dubaiconciergerie.demo.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(Long id);

    User create(UserDto dto);

    User update(Long id, UserDto dto);

    void delete(Long id);
}
