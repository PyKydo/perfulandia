package com.duoc.msvc.usuario.dtos;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class UsuarioUpdateDTO {
    @Schema(description = "Nombre del usuario", example = "María Fernanda")
    private String nombre;
    @Schema(description = "Apellido del usuario", example = "González Silva")
    private String apellido;
    @Schema(description = "Región de residencia", example = "Metropolitana de Santiago")
    private String region;
    @Schema(description = "Comuna de residencia", example = "Las Condes")
    private String comuna;
    @Schema(description = "Ciudad de residencia", example = "Santiago")
    private String ciudad;
    @Schema(description = "Código postal", example = "8320000")
    private String codigoPostal;
    @Schema(description = "Dirección de residencia", example = "Av. Apoquindo 4501, Depto 1201")
    private String direccion;
    @Schema(description = "Correo electrónico", example = "maria.gonzalez@outlook.com")
    private String correo;
    @Schema(description = "Contraseña de acceso (mínimo 8 caracteres)", example = "Perfume2024!")
    private String contrasena;
    @Schema(description = "Teléfono de contacto", example = "+56987654321")
    private String telefono;
} 