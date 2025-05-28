package com.duoc.msvc.pedido.dtos.pojos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class PagoClientDTO {
    private String metodo;
    private BigDecimal monto;
    private String estado;
    private String fecha;
    private Long idPedido;
}
