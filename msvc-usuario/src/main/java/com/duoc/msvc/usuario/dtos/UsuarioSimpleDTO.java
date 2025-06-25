package com.duoc.msvc.usuario.dtos;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class UsuarioSimpleDTO {
    @Schema(description = "ID del usuario", example = "1")
    private Long id;
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;
    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;
    @Schema(description = "Región de residencia", example = "Metropolitana")
    private String region;
    @Schema(description = "Comuna de residencia", example = "Providencia")
    private String comuna;
    @Schema(description = "Ciudad de residencia", example = "Santiago")
    private String ciudad;
    @Schema(description = "Código postal", example = "7500000")
    private String codigoPostal;
    @Schema(description = "Dirección de residencia", example = "Av. Providencia 123")
    private String direccion;
    @Schema(description = "Correo electrónico", example = "juan.perez@email.com")
    private String correo;
    @Schema(description = "Contraseña de acceso", example = "password123")
    private String contrasena;
    @Schema(description = "Teléfono de contacto", example = "+56912345678")
    private String telefono;
} 