package com.duoc.msvc.pedido.dtos.pojos;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para representar información básica de un envío desde otro microservicio")
public class EnvioClientDTO {
    @Schema(description = "Costo del envío", example = "3500.00")
    private BigDecimal costo;
    @Schema(description = "Dirección de entrega", example = "Av. Providencia 123")
    private String direccion;
    @Schema(description = "Ciudad de entrega", example = "Santiago")
    private String ciudad;
    @Schema(description = "Comuna de entrega", example = "Providencia")
    private String comuna;
    @Schema(description = "Región de entrega", example = "Metropolitana")
    private String region;
    @Schema(description = "Código postal de entrega", example = "7500000")
    private String codigoPostal;
    @Schema(description = "Estado del envío", example = "En camino")
    private String estado;
    @Schema(description = "ID del pedido asociado", example = "5")
    private Long idPedido;
}
