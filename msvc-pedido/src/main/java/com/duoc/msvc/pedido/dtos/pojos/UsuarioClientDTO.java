package com.duoc.msvc.pedido.dtos.pojos;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para representar información básica de un usuario desde otro microservicio")
public class UsuarioClientDTO {
    @Schema(description = "ID del usuario", example = "1")
    private Long idCliente;
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;
    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@email.com")
    private String correo;
    @Schema(description = "Dirección de residencia", example = "Av. Providencia 123")
    private String direccion;
    @Schema(description = "Ciudad de residencia", example = "Santiago")
    private String ciudad;
    @Schema(description = "Comuna de residencia", example = "Providencia")
    private String comuna;
    @Schema(description = "Región de residencia", example = "Metropolitana")
    private String region;
    @Schema(description = "Código postal", example = "7500000")
    private String codigoPostal;
}
