package com.duoc.msvc.envio.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para la creación de un envío")
public class EnvioCreateDTO {
    @Schema(description = "ID del pedido asociado", example = "10", required = true)
    @NotNull(message = "El ID del pedido es obligatorio")
    private Long idPedido;
    @Schema(description = "Dirección de entrega", example = "Av. Providencia 123", required = true)
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;
    @Schema(description = "Región de entrega", example = "Metropolitana", required = true)
    @NotBlank(message = "La región es obligatoria")
    private String region;
    @Schema(description = "Ciudad de entrega", example = "Santiago", required = true)
    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;
    @Schema(description = "Comuna de entrega", example = "Providencia", required = true)
    @NotBlank(message = "La comuna es obligatoria")
    private String comuna;
    @Schema(description = "Código postal", example = "7500000", required = true)
    @NotBlank(message = "El código postal es obligatorio")
    private String codigoPostal;
    @Schema(description = "Costo del envío", example = "2500")
    private BigDecimal costo;
} 