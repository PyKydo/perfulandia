package com.duoc.msvc.envio.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO de respuesta simple para envíos")
public class EnvioGetDTO {
    @Schema(description = "ID del envío", example = "1")
    private Long id;
    @Schema(description = "ID del pedido asociado", example = "10")
    private Long idPedido;
    @Schema(description = "Dirección de entrega", example = "Av. Providencia 123")
    private String direccion;
    @Schema(description = "Región de entrega", example = "Metropolitana")
    private String region;
    @Schema(description = "Ciudad de entrega", example = "Santiago")
    private String ciudad;
    @Schema(description = "Comuna de entrega", example = "Providencia")
    private String comuna;
    @Schema(description = "Código postal", example = "7500000")
    private String codigoPostal;
    @Schema(description = "Costo del envío", example = "2500")
    private BigDecimal costo;
    @Schema(description = "Estado del envío", example = "Pendiente")
    private String estado;
    @Schema(description = "Fecha estimada de entrega", example = "01/01/2024 12:00:00")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime fechaEstimadaEntrega;
} 