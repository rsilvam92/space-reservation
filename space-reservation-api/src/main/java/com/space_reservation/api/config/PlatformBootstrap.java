package com.space_reservation.api.config;

import com.space_reservation.api.entity.Role;
import com.space_reservation.api.entity.User;
import com.space_reservation.api.entity.enums.UserStatus;
import com.space_reservation.api.repository.RoleRepository;
import com.space_reservation.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@EnableConfigurationProperties(SuperAdminBootstrapProperties.class)
public class PlatformBootstrap implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformBootstrap.class);
    private static final String SUPER_ADMIN_ROLE = "SUPER_ADMIN";

    private final SuperAdminBootstrapProperties properties;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public PlatformBootstrap(
            SuperAdminBootstrapProperties properties,
            RoleRepository roleRepository,
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.properties = properties;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments arguments) {
        ensureRole("RESIDENTE", "Residente del condominio");
        ensureRole("ADMIN", "Administrador de un condominio");
        Role superAdminRole = ensureRole(SUPER_ADMIN_ROLE, "Administrador de la plataforma RESM");

        if (!properties.enabled()) {
            LOGGER.info("Bootstrap de SUPER_ADMIN deshabilitado");
            return;
        }

        String email = requireValue(properties.email(), "SUPER_ADMIN_EMAIL").toLowerCase(Locale.ROOT);
        String password = requireValue(properties.password(), "SUPER_ADMIN_PASSWORD");
        String document = requireValue(properties.document(), "SUPER_ADMIN_DOCUMENT");

        if (password.length() < 12) {
            throw new IllegalStateException("SUPER_ADMIN_PASSWORD debe tener al menos 12 caracteres");
        }

        userRepository.findByCorreo(email).ifPresentOrElse(
                existing -> validateExistingSuperAdmin(existing, superAdminRole),
                () -> createSuperAdmin(email, password, document, superAdminRole)
        );
    }

    private Role ensureRole(String name, String description) {
        return roleRepository.findByNombre(name).orElseGet(() -> {
            Role role = new Role();
            role.setNombre(name);
            role.setDescripcion(description);
            return roleRepository.save(role);
        });
    }

    private void createSuperAdmin(String email, String password, String document, Role role) {
        if (userRepository.existsByDocumento(document)) {
            throw new IllegalStateException("SUPER_ADMIN_DOCUMENT ya pertenece a otro usuario");
        }

        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setNombre("Administrador");
        user.setApellido("Plataforma");
        user.setCorreo(email);
        user.setDocumento(document);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setEstado(UserStatus.ACTIVE);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        userRepository.save(user);

        LOGGER.info("SUPER_ADMIN creado correctamente para {}", email);
    }

    private void validateExistingSuperAdmin(User existing, Role superAdminRole) {
        if (existing.getRole() == null || !SUPER_ADMIN_ROLE.equals(existing.getRole().getNombre())) {
            throw new IllegalStateException(
                    "SUPER_ADMIN_EMAIL ya pertenece a un usuario que no es SUPER_ADMIN"
            );
        }

        if (existing.getEstado() != UserStatus.ACTIVE) {
            existing.setEstado(UserStatus.ACTIVE);
            existing.setUpdatedAt(LocalDateTime.now());
            userRepository.save(existing);
        }

        LOGGER.info("SUPER_ADMIN ya existe para {}; no se creó un duplicado", existing.getCorreo());
    }

    private String requireValue(String value, String environmentVariable) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(environmentVariable + " es obligatoria cuando el bootstrap está habilitado");
        }
        return value.trim();
    }
}
