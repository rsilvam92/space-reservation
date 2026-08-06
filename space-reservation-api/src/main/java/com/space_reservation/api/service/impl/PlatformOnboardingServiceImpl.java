package com.space_reservation.api.service.impl;

import com.space_reservation.api.config.AccountFlowProperties;
import com.space_reservation.api.dto.request.AcceptAdministratorInvitationDTO;
import com.space_reservation.api.dto.request.AdministratorInvitationRequestDTO;
import com.space_reservation.api.dto.request.CondominiumInputDTO;
import com.space_reservation.api.dto.request.CondominiumOnboardingRequestDTO;
import com.space_reservation.api.dto.response.AdministratorInvitationResponseDTO;
import com.space_reservation.api.dto.response.CondominiumOnboardingResponseDTO;
import com.space_reservation.api.dto.response.MessageResponseDTO;
import com.space_reservation.api.entity.AdministrativeMembership;
import com.space_reservation.api.entity.AdministratorInvitation;
import com.space_reservation.api.entity.Condominium;
import com.space_reservation.api.entity.Role;
import com.space_reservation.api.entity.User;
import com.space_reservation.api.entity.enums.UserStatus;
import com.space_reservation.api.exception.BusinessException;
import com.space_reservation.api.exception.ResourceNotFoundException;
import com.space_reservation.api.repository.AdministrativeMembershipRepository;
import com.space_reservation.api.repository.AdministratorInvitationRepository;
import com.space_reservation.api.repository.CondominiumRepository;
import com.space_reservation.api.repository.RoleRepository;
import com.space_reservation.api.repository.UserRepository;
import com.space_reservation.api.security.CurrentUserProvider;
import com.space_reservation.api.security.SecureTokenGenerator;
import com.space_reservation.api.service.PlatformOnboardingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(AccountFlowProperties.class)
public class PlatformOnboardingServiceImpl implements PlatformOnboardingService {

    private static final String ADMIN_ROLE = "ADMIN";

    private final CondominiumRepository condominiumRepository;
    private final AdministratorInvitationRepository invitationRepository;
    private final AdministrativeMembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CurrentUserProvider currentUserProvider;
    private final SecureTokenGenerator tokenGenerator;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AccountFlowProperties properties;

    @Override
    @Transactional
    public CondominiumOnboardingResponseDTO onboardCondominium(CondominiumOnboardingRequestDTO request) {
        CondominiumInputDTO input = request.condominium();
        String name = input.nombre().trim();
        if (condominiumRepository.existsByNombreIgnoreCase(name)) {
            throw new BusinessException("Ya existe un condominio con ese nombre.");
        }

        LocalDateTime now = LocalDateTime.now();
        Condominium condominium = new Condominium();
        condominium.setNombre(name);
        condominium.setDireccion(input.direccion().trim());
        condominium.setCiudad(input.ciudad().trim());
        condominium.setTipoSector(normalize(input.tipoSector()));
        condominium.setActivo(true);
        condominium.setCreatedAt(now);
        condominium.setUpdatedAt(now);
        Condominium saved = condominiumRepository.save(condominium);

        return new CondominiumOnboardingResponseDTO(
                saved,
                createInvitation(saved, request.administratorEmail())
        );
    }

    @Override
    @Transactional
    public AdministratorInvitationResponseDTO inviteAdministrator(
            Long condominiumId,
            AdministratorInvitationRequestDTO request
    ) {
        Condominium condominium = condominiumRepository.findById(condominiumId)
                .orElseThrow(() -> new ResourceNotFoundException("El condominio no existe."));
        return createInvitation(condominium, request.email());
    }

    @Override
    @Transactional
    public MessageResponseDTO acceptInvitation(AcceptAdministratorInvitationDTO request) {
        LocalDateTime now = LocalDateTime.now();
        AdministratorInvitation invitation = invitationRepository
                .findByTokenHashAndUsedAtIsNull(tokenGenerator.digest(request.token()))
                .filter(item -> item.getExpiresAt().isAfter(now))
                .orElseThrow(() -> new BusinessException("La invitación no existe, expiró o ya fue utilizada."));

        if (userRepository.existsByCorreoIgnoreCase(invitation.getEmail())) {
            throw new BusinessException("Ya existe una cuenta con el correo de la invitación.");
        }
        if (userRepository.existsByDocumento(request.documento().trim())) {
            throw new BusinessException("El número de documento ya está registrado.");
        }

        Role role = roleRepository.findByNombre(ADMIN_ROLE)
                .orElseThrow(() -> new BusinessException("El rol ADMIN no está configurado."));

        User administrator = new User();
        administrator.setNombre(request.nombre().trim());
        administrator.setApellido(request.apellido().trim());
        administrator.setDocumento(request.documento().trim());
        administrator.setCorreo(invitation.getEmail());
        administrator.setTelefono(normalize(request.telefono()));
        administrator.setPassword(passwordEncoder.encode(request.password()));
        administrator.setRole(role);
        administrator.setEstado(UserStatus.ACTIVE);
        administrator.setCreatedAt(now);
        administrator.setUpdatedAt(now);
        User savedAdministrator = userRepository.save(administrator);

        AdministrativeMembership membership = new AdministrativeMembership();
        membership.setUser(savedAdministrator);
        membership.setCondominium(invitation.getCondominium());
        membership.setCreatedAt(now);
        membershipRepository.save(membership);

        invitation.setUsedAt(now);
        return new MessageResponseDTO("Cuenta administrativa activada. Ya puedes iniciar sesión.");
    }

    private AdministratorInvitationResponseDTO createInvitation(Condominium condominium, String rawEmail) {
        String email = rawEmail.trim().toLowerCase(Locale.ROOT);
        if (userRepository.existsByCorreoIgnoreCase(email)) {
            throw new BusinessException("Ya existe una cuenta con ese correo electrónico.");
        }

        LocalDateTime now = LocalDateTime.now();
        invitationRepository
                .findFirstByCondominiumIdAndEmailIgnoreCaseAndUsedAtIsNullOrderByCreatedAtDesc(
                        condominium.getId(),
                        email
                )
                .ifPresent(existing -> {
                    if (existing.getExpiresAt().isAfter(now)) {
                        throw new BusinessException("Ya existe una invitación vigente para ese correo.");
                    }
                    existing.setUsedAt(now);
                });

        String token = tokenGenerator.generate();
        LocalDateTime expiresAt = now.plusHours(properties.invitationExpirationHours());
        AdministratorInvitation invitation = new AdministratorInvitation();
        invitation.setCondominium(condominium);
        invitation.setCreatedBy(currentUserProvider.requireUser());
        invitation.setEmail(email);
        invitation.setTokenHash(tokenGenerator.digest(token));
        invitation.setCreatedAt(now);
        invitation.setExpiresAt(expiresAt);
        AdministratorInvitation saved = invitationRepository.save(invitation);

        String link = properties.webBaseUrl() + "/activate-administrator?token=" + token;
        log.info("Invitación local para {}: {}", email, link);

        return new AdministratorInvitationResponseDTO(
                saved.getId(),
                condominium.getId(),
                condominium.getNombre(),
                email,
                expiresAt,
                link
        );
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
