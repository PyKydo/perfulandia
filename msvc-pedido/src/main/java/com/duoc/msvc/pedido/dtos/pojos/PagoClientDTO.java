package com.duoc.msvc.pedido.dtos.pojos;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para representar información básica de un pago desde otro microservicio")
public class PagoClientDTO {
    @Schema(description = "Método de pago utilizado", example = "Tarjeta de Crédito")
    private String metodoPago;
    @Schema(description = "Monto pagado", example = "12000.00")
    private BigDecimal monto;
    @Schema(description = "Estado del pago", example = "Pagado")
    private String estado;
    @Schema(description = "Fecha del pago", example = "2024-03-20T15:30:00.000Z")
    private String fecha;
    @Schema(description = "ID del pedido asociado", example = "5")
    private Long idPedido;
}
