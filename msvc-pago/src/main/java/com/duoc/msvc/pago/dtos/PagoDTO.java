package com.duoc.msvc.pago.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class PagoDTO {
    private Long id;
    private Long idPedido;
    private String metodo;
    private BigDecimal monto;
    private String estado;
    private String fecha;
}
