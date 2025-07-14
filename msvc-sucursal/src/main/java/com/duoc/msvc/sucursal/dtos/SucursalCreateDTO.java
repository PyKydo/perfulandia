package com.duoc.msvc.sucursal.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para la creación de una sucursal")
public class SucursalCreateDTO {
    @Schema(description = "Dirección de la sucursal", example = "Av. Apoquindo 4501, Local 1201")
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;
    @Schema(description = "Región de la sucursal", example = "Metropolitana de Santiago")
    @NotBlank(message = "La región es obligatoria")
    private String region;
    @Schema(description = "Comuna de la sucursal", example = "Las Condes")
    @NotBlank(message = "La comuna es obligatoria")
    private String comuna;
    @Schema(description = "Cantidad de personal", example = "8")
    @NotNull(message = "La cantidad de personal es obligatoria")
    @Min(value = 1, message = "Debe haber al menos 1 persona")
    private Integer cantidadPersonal;
    @Schema(description = "Horarios de atención", example = "Lunes a Viernes: 10:00 - 20:00, Sábados: 10:00 - 18:00")
    @NotBlank(message = "El horario de atención es obligatorio")
    private String horariosAtencion;
} 