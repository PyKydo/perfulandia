package com.duoc.msvc.pedido.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO de detalle de pedido")
public class DetallePedidoDTO {
    @Schema(description = "ID del producto", example = "5")
    private Long idProducto;
    @Schema(description = "Nombre del producto", example = "Perfume Elegante")
    private String nombreProducto;
    @Schema(description = "Marca del producto", example = "Chanel")
    private String marcaProducto;
    @Schema(description = "Cantidad solicitada", example = "2")
    private Integer cantidad;
    @Schema(description = "Precio unitario del producto", example = "59990.00")
    private BigDecimal precio;
}
