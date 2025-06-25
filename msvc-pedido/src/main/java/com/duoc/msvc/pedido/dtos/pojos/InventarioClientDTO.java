package com.duoc.msvc.pedido.dtos.pojos;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para representar información básica de inventario desde otro microservicio")
public class InventarioClientDTO {
    @JsonProperty("id")
    @Schema(description = "ID del inventario", example = "100")
    private Long idInventario;
    @Schema(description = "ID del producto asociado", example = "1")
    private Long idProducto;
    @Schema(description = "Stock disponible del producto en la sucursal", example = "25")
    private Integer stock;
}
