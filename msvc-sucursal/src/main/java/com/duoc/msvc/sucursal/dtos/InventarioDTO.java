package com.duoc.msvc.sucursal.dtos;

import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class InventarioDTO {
    private Long idInventario;
    private Long idProducto;
    private Integer stock;
}
