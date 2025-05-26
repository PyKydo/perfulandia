package com.duoc.msvc.pedido.dtos;


import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class PedidoDTO {
    private String nombreCliente;
    private String apellidoCliente;
    private String direccion;
    private String correo;
    private BigDecimal total;
    private BigDecimal costoEnvio;
    private String estado;
    private List<DetallePedidoDTO> detallesPedido;
}
