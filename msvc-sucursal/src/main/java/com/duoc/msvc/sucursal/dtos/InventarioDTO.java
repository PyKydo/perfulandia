package com.duoc.msvc.sucursal.dtos;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para representar información de inventario en una sucursal, incluyendo nombre y marca del producto")
public class InventarioDTO {
    @Schema(description = "ID del inventario", example = "100")
    private Long id;
    @Schema(description = "ID del producto asociado", example = "5")
    private Long idProducto;
    @Schema(description = "Nombre del producto", example = "Chanel N°5 Eau de Parfum")
    private String nombreProducto;
    @Schema(description = "Marca del producto", example = "Chanel")
    private String marcaProducto;
    @Schema(description = "Stock disponible del producto en la sucursal", example = "25")
    private Integer stock;
}
