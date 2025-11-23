package com.ibrahim.dubaiconciergerie.demo.service;

import com.ibrahim.dubaiconciergerie.demo.dto.UserDto;
import com.ibrahim.dubaiconciergerie.demo.entity.User;

import java.util.List;

public interface UserService {

    User create(UserDto dto);

    List<User> getAll();

    User getById(Long id);

    User update(Long id, UserDto dto);

    void delete(Long id);
}
