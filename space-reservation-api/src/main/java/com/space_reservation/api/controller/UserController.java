package com.space_reservation.api.controller;

import com.space_reservation.api.entity.User;
import com.space_reservation.api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @PutMapping("/{id}/approve")
    public void approve(
            @PathVariable Long id,
            HttpServletRequest request
    ) {

        String role = (String) request.getAttribute("userRole");

        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("No autorizado");
        }

        userService.approveUser(id);
    }
}