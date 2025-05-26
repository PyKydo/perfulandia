package com.duoc.msvc.envio.dtos.pojos;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class PedidoDTO {
    private Long idCliente;
    private BigDecimal total;
    private String estado;
    private List<DetallePedidoDTO> detallesPedido;
}
