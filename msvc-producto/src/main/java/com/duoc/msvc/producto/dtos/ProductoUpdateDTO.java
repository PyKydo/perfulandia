package com.duoc.msvc.producto.dtos;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para la actualización de un producto")
public class ProductoUpdateDTO {
    
    @Schema(description = "Nombre del producto", example = "Perfume Elegante")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @Schema(description = "Marca del producto", example = "Chanel")
    @NotBlank(message = "La marca es obligatoria")
    private String marca;
    
    @Schema(description = "Precio del producto", example = "59990.00")
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    private BigDecimal precio;
    
    @Schema(description = "Descripción del producto", example = "Perfume elegante con notas de madera y especias")
    private String descripcion;
    
    @Schema(description = "URL de la imagen representativa", example = "https://ejemplo.com/imagen.jpg")
    private String imagenRepresentativaURL;
    
    @Schema(description = "Categoría del producto", example = "Perfumes")
    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;
    
    @Schema(description = "Porcentaje de concentración de la fragancia", example = "85.5")
    @NotNull(message = "El porcentaje de concentración es obligatorio")
    @DecimalMin(value = "0.0", message = "El porcentaje no puede ser negativo")
    private Double porcentajeConcentracion;
} 