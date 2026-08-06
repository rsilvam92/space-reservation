package com.space_reservation.api.service.impl;

import com.space_reservation.api.dto.request.UserRegisterDTO;
import com.space_reservation.api.dto.response.PendingUserDTO;
import com.space_reservation.api.entity.Apartment;
import com.space_reservation.api.entity.Role;
import com.space_reservation.api.entity.User;
import com.space_reservation.api.entity.enums.UserStatus;
import com.space_reservation.api.exception.BusinessException;
import com.space_reservation.api.exception.ResourceNotFoundException;
import com.space_reservation.api.mapper.UserMapper;
import com.space_reservation.api.repository.ApartmentRepository;
import com.space_reservation.api.repository.RoleRepository;
import com.space_reservation.api.repository.UserRepository;
import com.space_reservation.api.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ApartmentRepository apartmentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerUser(UserRegisterDTO dto) {
        if (userRepository.findByCorreo(dto.getCorreo()).isPresent()) {
            throw new BusinessException("El correo electrónico ya está registrado");
        }

        User user = UserMapper.toEntity(dto);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.setEstado(UserStatus.PENDING);
        user.setCreatedAt(LocalDateTime.now());

        Apartment apartment = apartmentRepository.findById(dto.getApartamentoId())
                .orElseThrow(() -> new ResourceNotFoundException("El apartamento seleccionado no existe"));
        user.setApartment(apartment);

        Role defaultRole = roleRepository.findByNombre("RESIDENTE")
                .orElseThrow(() -> new ResourceNotFoundException("El rol RESIDENTE no está configurado en el sistema"));
        user.setRole(defaultRole);

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String correo) {
        return userRepository.findByCorreo(correo);
    }

    @Override
    public void approveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        user.setEstado(UserStatus.ACTIVE);
        userRepository.save(user);
    }

    @Override
    public User registerAdmin(UserRegisterDTO dto) {
        if (userRepository.existsByCorreo(dto.getCorreo())) {
            throw new BusinessException("El correo ya está registrado.");
        }

        Apartment apartment = apartmentRepository.findById(dto.getApartamentoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El apartamento con ID " + dto.getApartamentoId() + " no existe en el sistema."
                ));

        Role adminRole = roleRepository.findByNombre("ADMIN")
                .orElseThrow(() -> new ResourceNotFoundException("El rol ADMIN no está configurado."));

        User admin = new User();
        admin.setNombre(dto.getNombre());
        admin.setApellido(dto.getApellido());
        admin.setCorreo(dto.getCorreo());
        admin.setDocumento(dto.getDocumento());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));

        admin.setApartment(apartment);
        admin.setRole(adminRole);
        admin.setEstado(UserStatus.ACTIVE);

        return userRepository.save(admin);
    }

    @Override
    public List<PendingUserDTO> getPendingUsers() {

        return userRepository.findByEstado(UserStatus.PENDING)
                .stream()
                .map(UserMapper::toPendingDTO)
                .toList();

    }
}