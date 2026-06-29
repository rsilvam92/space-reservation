package com.space_reservation.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class UserRegisterDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Email(message = "Correo inválido")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @NotBlank(message = "Documento obligatorio")
    @Pattern(regexp = "^\\d+$", message = "El documento debe contener únicamente números (sin letras, puntos ni guiones)")
    private String documento;

    @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
    private String password;

    private String telefono;
    private String tipoOcupante;

    @NotNull(message = "Debe seleccionar un apartamento")
    private Long apartamentoId;
}