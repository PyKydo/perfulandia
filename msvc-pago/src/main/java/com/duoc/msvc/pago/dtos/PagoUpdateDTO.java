package com.duoc.msvc.pago.dtos;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para la actualización de un pago")
public class PagoUpdateDTO {
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal monto;
    @Schema(description = "Nuevo estado del pago", example = "Pagado")
    @NotBlank
    private String estado;
    @Schema(description = "Nuevo método de pago", example = "Transferencia Bancaria")
    @NotBlank
    private String metodoPago;
    @NotNull
    private Long idPedido;
} 