package com.duoc.msvc.pedido.dtos.pojos;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para representar información básica de un producto desde otro microservicio")
public class ProductoClientDTO {
    @Schema(description = "Nombre del producto", example = "Perfume Elegante")
    private String nombre;
    @Schema(description = "Marca del producto", example = "Chanel")
    private String marca;
    @Schema(description = "Precio del producto", example = "59990.00")
    private BigDecimal precio;
    @Schema(description = "Categoría del producto", example = "Perfumes")
    private String categoria;
}
