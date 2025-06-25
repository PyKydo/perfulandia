package com.duoc.msvc.envio.dtos.pojos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "DTO para representar el detalle de un pedido asociado al envío")
public class DetallePedidoDTO {
    @Schema(description = "ID del producto", example = "5")
    private Long idProducto;
    @Schema(description = "Cantidad solicitada", example = "2")
    private Integer cantidad;
    @Schema(description = "Precio unitario del producto", example = "59990.00")
    private BigDecimal precio;
}
