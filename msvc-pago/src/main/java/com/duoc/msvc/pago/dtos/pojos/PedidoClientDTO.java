package com.duoc.msvc.pago.dtos.pojos;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para representar información básica de un pedido asociado al pago")
public class PedidoClientDTO {
    @Schema(description = "ID del cliente", example = "1")
    private Long idCliente;
    @Schema(description = "Total del pedido", example = "120000.00")
    private BigDecimal total;
    @Schema(description = "Estado del pedido", example = "Pagado")
    private String estado;
    @Schema(description = "Lista de detalles del pedido")
    private List<DetallePedidoClientDTO> detallesPedido;
}
