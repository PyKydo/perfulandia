package com.duoc.msvc.pedido.dtos.pojos;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class InventarioClientDTO {
    @JsonProperty("id")
    private Long idInventario;
    private Long idProducto;
    private Integer stock;
}
