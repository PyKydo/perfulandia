package com.duoc.msvc.usuario.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class UsuarioCreateDTO {
    @Schema(description = "Nombre del usuario", example = "María Fernanda")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @Schema(description = "Apellido del usuario", example = "González Silva")
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    
    @Schema(description = "Región de residencia", example = "Metropolitana de Santiago")
    @NotBlank(message = "La región es obligatoria")
    private String region;
    
    @Schema(description = "Comuna de residencia", example = "Las Condes")
    @NotBlank(message = "La comuna es obligatoria")
    private String comuna;
    
    @Schema(description = "Ciudad de residencia", example = "Santiago")
    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;
    
    @Schema(description = "Código postal", example = "8320000")
    @NotBlank(message = "El código postal es obligatorio")
    private String codigoPostal;
    
    @Schema(description = "Dirección de residencia", example = "Av. Apoquindo 4501, Depto 1201")
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;
    
    @Schema(description = "Correo electrónico", example = "maria.gonzalez@outlook.com")
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    private String correo;
    
    @Schema(description = "Contraseña de acceso (mínimo 8 caracteres)", example = "Perfume2024!")
    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;
    
    @Schema(description = "Teléfono de contacto", example = "+56987654321")
    private String telefono;
} 