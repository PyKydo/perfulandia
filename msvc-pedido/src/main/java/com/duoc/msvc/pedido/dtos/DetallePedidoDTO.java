package com.duoc.msvc.pedido.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class DetallePedidoDTO {
    private Long idProducto;
    private String nombreProducto;
    private String marcaProducto;
    private Integer cantidad;
    private BigDecimal precio;
}
