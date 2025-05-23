package com.duoc.msvc.pedido.dtos;

import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class DetallePedidoDTO {
    private Long idProducto;
    private Integer cantidad;
}
