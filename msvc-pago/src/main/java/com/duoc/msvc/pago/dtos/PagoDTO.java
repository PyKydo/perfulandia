package com.duoc.msvc.pago.dtos;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO interno para operaciones básicas de pago")
public class PagoDTO {
    @Schema(description = "ID del pago", example = "1")
    private Long id;
    @Schema(description = "ID del pedido asociado", example = "10")
    private Long idPedido;
    @Schema(description = "Método de pago utilizado", example = "Tarjeta de Crédito")
    private String metodoPago;
    @Schema(description = "Monto pagado", example = "12000.00")
    private Double monto;
    @Schema(description = "Estado actual del pago", example = "Pagado")
    private String estado;
    private LocalDateTime fecha;
}
