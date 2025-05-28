package com.duoc.msvc.usuario.dtos.pojos;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class DetallePedidoClientDTO {
    private Long idProducto;
    private String nombreProducto;
    private String marcaProducto;
    private Integer cantidad;
    private BigDecimal precio;
}
