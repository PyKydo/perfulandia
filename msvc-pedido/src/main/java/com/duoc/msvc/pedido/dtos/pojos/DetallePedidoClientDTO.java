package com.duoc.msvc.pedido.dtos.pojos;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class DetallePedidoClientDTO {
    @Schema(description = "ID del producto", example = "5")
    private Long idProducto;
    @Schema(description = "Nombre del producto", example = "Chanel N°5 Eau de Parfum")
    private String nombreProducto;
    @Schema(description = "Marca del producto", example = "Chanel")
    private String marcaProducto;
    @Schema(description = "Cantidad solicitada", example = "1")
    private Integer cantidad;
    @Schema(description = "Precio unitario del producto", example = "89980.00")
    private BigDecimal precio;
} 