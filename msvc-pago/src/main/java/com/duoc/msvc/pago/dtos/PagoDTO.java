package com.duoc.msvc.pago.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class PagoDTO {
    private String metodo;
    private BigDecimal monto;
    private String estado;
    private String fecha;
}
