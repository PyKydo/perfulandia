package com.duoc.msvc.pago.dtos;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class PagoCreateDTO {
    @NotNull
    private Long idPedido;
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal monto;
    @NotBlank
    private String metodoPago;
} 