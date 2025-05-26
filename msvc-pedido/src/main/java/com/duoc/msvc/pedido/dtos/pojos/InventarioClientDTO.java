package com.duoc.msvc.pedido.dtos.pojos;

import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class InventarioClientDTO {
    private Long idInventario;
    private Long idProducto;
    private Integer stock;
}
