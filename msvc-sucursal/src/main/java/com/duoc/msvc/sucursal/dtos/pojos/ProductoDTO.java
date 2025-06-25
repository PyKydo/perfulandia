package com.duoc.msvc.sucursal.dtos.pojos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para representar información básica de un producto en la sucursal")
public class ProductoDTO {
    @Schema(description = "ID del producto", example = "1")
    private Long id;
    @Schema(description = "Nombre del producto", example = "Perfume Elegante")
    private String nombre;
    @Schema(description = "Marca del producto", example = "Chanel")
    private String marca;
}
