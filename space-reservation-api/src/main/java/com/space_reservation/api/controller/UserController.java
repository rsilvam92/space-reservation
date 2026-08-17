package com.space_reservation.api.controller;

import com.space_reservation.api.dto.request.UserRegisterDTO;
import com.space_reservation.api.dto.response.PendingUserDTO;
import com.space_reservation.api.dto.response.RegistrationApartmentDTO;
import com.space_reservation.api.dto.response.RegistrationCondominiumDTO;
import com.space_reservation.api.dto.response.UserResponseDTO;
import com.space_reservation.api.entity.User;
import com.space_reservation.api.exception.BusinessException;
import com.space_reservation.api.exception.ResourceNotFoundException;
import com.space_reservation.api.mapper.UserMapper;
import com.space_reservation.api.repository.ApartmentRepository;
import com.space_reservation.api.repository.CondominiumRepository;
import com.space_reservation.api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CondominiumRepository condominiumRepository;
    private final ApartmentRepository apartmentRepository;

    @GetMapping("/register/condominiums")
    public List<RegistrationCondominiumDTO> getRegistrationCondominiums() {
        return condominiumRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(condominium -> new RegistrationCondominiumDTO(
                        condominium.getId(),
                        condominium.getNombre(),
                        condominium.getCiudad()
                ))
                .toList();
    }

    @GetMapping("/register/condominiums/{condominiumId}/apartments")
    public List<RegistrationApartmentDTO> getRegistrationApartments(
            @PathVariable Long condominiumId
    ) {
        if (!condominiumRepository.existsById(condominiumId)) {
            throw new ResourceNotFoundException("El condominio seleccionado no existe");
        }

        return apartmentRepository.findByCondominiumIdOrderBySectorAscNumeroAsc(condominiumId)
                .stream()
                .map(apartment -> new RegistrationApartmentDTO(
                        apartment.getId(),
                        apartment.getSector(),
                        apartment.getNumero(),
                        apartment.getEstado()
                ))
                .toList();
    }

    @PostMapping("/register")
    public UserResponseDTO register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        User savedUser = userService.registerUser(registerDTO);
        return UserMapper.toDTO(savedUser);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Map<String, Object>> approve(
            @Valid
            @PathVariable Long id,
            HttpServletRequest request
    ) {

        String role = (String) request.getAttribute("userRole");

        if (!"ADMIN".equals(role)) {
            throw new BusinessException("No tienes permisos de administrador para aprobar usuarios.");
        }

        userService.approveUser(id);

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        response.put("status", 200);
        response.put("message", "Usuario con ID " + id + " aprobado exitosamente. Ahora puede iniciar sesión.");
        response.put("nuevoEstado", "ACTIVE");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin")
    public ResponseEntity<Map<String, Object>> createAdmin(
            @Valid @RequestBody UserRegisterDTO registerDTO,
            HttpServletRequest request
    ) {
        // 1. Validar que quien intenta crear al admin ya sea un ADMIN
        String currentRole = (String) request.getAttribute("userRole");
        if (!"ADMIN".equals(currentRole)) {
            throw new BusinessException("No tienes permisos para crear usuarios administradores.");
        }

        // 2. Registrar al nuevo administrador (con lógica especial en el servicio)
        User newAdmin = userService.registerAdmin(registerDTO);

        // 3. Responder un JSON de éxito
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        response.put("status", 201);
        response.put("message", "Administrador '" + newAdmin.getNombre() + "' creado exitosamente.");
        response.put("role", "ADMIN");
        response.put("estado", "ACTIVE");

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/pending")
    public List<PendingUserDTO> getPendingUsers(){
        return userService.getPendingUsers();
    }
}
