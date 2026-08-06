package com.space_reservation.api.service;

import com.space_reservation.api.dto.request.UserRegisterDTO;
import com.space_reservation.api.dto.response.PendingUserDTO;
import com.space_reservation.api.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerUser(UserRegisterDTO user);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    Optional<User> getUserByEmail(String email);

    void approveUser(Long userId);

    User registerAdmin(UserRegisterDTO user);
    List<PendingUserDTO> getPendingUsers();
}