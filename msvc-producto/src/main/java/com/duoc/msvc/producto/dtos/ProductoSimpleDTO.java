package com.duoc.msvc.producto.dtos;

import lombok.*;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO simple para representar un producto")
public class ProductoSimpleDTO {
    @Schema(description = "ID del producto", example = "1")
    private Long id;
    @Schema(description = "Nombre del producto", example = "Perfume Elegante")
    private String nombre;
    @Schema(description = "Marca del producto", example = "Chanel")
    private String marca;
    @Schema(description = "Precio del producto", example = "59990.00")
    private BigDecimal precio;
    @Schema(description = "Descripción del producto", example = "Perfume elegante con notas de madera y especias")
    private String descripcion;
    @Schema(description = "URL de la imagen representativa", example = "https://ejemplo.com/imagen.jpg")
    private String imagenRepresentativaURL;
    @Schema(description = "Categoría del producto", example = "Perfumes")
    private String categoria;
    @Schema(description = "Porcentaje de concentración de la fragancia", example = "85.5")
    private Double porcentajeConcentracion;
} 