package com.space_reservation.api.service;

import com.space_reservation.api.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerUser(User user);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    Optional<User> getUserByEmail(String email);

    void approveUser(Long userId);
}