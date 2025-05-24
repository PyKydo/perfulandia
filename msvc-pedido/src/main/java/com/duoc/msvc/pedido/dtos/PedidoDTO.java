package com.duoc.msvc.pedido.dtos;


import com.duoc.msvc.pedido.models.entities.DetallePedido;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class PedidoDTO {
    private Long idPedido;
    private Long idCliente;
    private BigDecimal total;
    private String estado;
    private List<DetallePedidoDTO> detallesPedido;
}
