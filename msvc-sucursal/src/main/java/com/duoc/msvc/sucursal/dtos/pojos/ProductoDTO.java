package com.duoc.msvc.sucursal.dtos.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "DTO para representar información básica de un producto en la sucursal")
public class ProductoDTO {
    @Schema(description = "ID del producto", example = "5")
    private Long id;
    @Schema(description = "Nombre del producto", example = "Chanel N°5 Eau de Parfum")
    private String nombre;
    @Schema(description = "Marca del producto", example = "Chanel")
    private String marca;
}
