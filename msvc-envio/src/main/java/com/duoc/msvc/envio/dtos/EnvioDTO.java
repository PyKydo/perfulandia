package com.duoc.msvc.envio.dtos;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO interno para operaciones básicas de envío")
public class EnvioDTO {
    @Schema(description = "ID del envío", example = "1")
    private Long id;
    @Schema(description = "ID del pedido asociado", example = "10")
    private Long idPedido;
    @Schema(description = "Dirección de entrega", example = "Av. Providencia 123")
    private String direccion;
    @Schema(description = "Región de entrega", example = "Metropolitana")
    private String region;
    @Schema(description = "Costo del envío", example = "2500")
    private BigDecimal costo;
    @Schema(description = "Ciudad de entrega", example = "Santiago")
    private String ciudad;
    @Schema(description = "Comuna de entrega", example = "Providencia")
    private String comuna;
    @Schema(description = "Código postal", example = "7500000")
    private String codigoPostal;
    @Schema(description = "Estado del envío", example = "Pendiente")
    private String estado;
}
