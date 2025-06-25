package com.duoc.msvc.envio.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para la actualización de un envío")
public class EnvioUpdateDTO {
    @Schema(description = "Dirección de entrega", example = "Av. Providencia 123")
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;
    @Schema(description = "Región de entrega", example = "Metropolitana")
    @NotBlank(message = "La región es obligatoria")
    private String region;
    @Schema(description = "Ciudad de entrega", example = "Santiago")
    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;
    @Schema(description = "Comuna de entrega", example = "Providencia")
    @NotBlank(message = "La comuna es obligatoria")
    private String comuna;
    @Schema(description = "Código postal", example = "7500000")
    @NotBlank(message = "El código postal es obligatorio")
    private String codigoPostal;
    @Schema(description = "Estado del envío", example = "Enviado")
    @NotBlank(message = "El estado es obligatorio")
    private String estado;
    @Schema(description = "Fecha estimada de entrega", example = "01/01/2024 12:00:00")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime fechaEstimadaEntrega;
} 