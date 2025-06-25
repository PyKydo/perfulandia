package com.duoc.msvc.pago.dtos;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO de respuesta simple para pagos")
public class PagoGetDTO {
    @Schema(description = "ID del pago", example = "1")
    private Long id;
    @Schema(description = "ID del pedido asociado", example = "10")
    private Long idPedido;
    @Schema(description = "Método de pago utilizado", example = "Tarjeta de Crédito")
    private String metodoPago;
    @Schema(description = "Monto pagado", example = "12000.00")
    private BigDecimal monto;
    @Schema(description = "Estado actual del pago", example = "Pagado")
    private String estado;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime fechaPago;
} 