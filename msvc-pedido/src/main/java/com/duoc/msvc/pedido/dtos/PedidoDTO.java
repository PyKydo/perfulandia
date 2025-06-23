package com.duoc.msvc.pedido.dtos;


import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class PedidoDTO {
    private Long id;
    private Long idCliente;
    private String nombreCliente;
    private String apellidoCliente;
    private String direccion;
    private String correo;
    private String region;
    private String comuna;
    private List<DetallePedidoDTO> detallesPedido;
    private BigDecimal costoEnvio;
    private BigDecimal totalDetalles;
    private BigDecimal montoFinal;
    private String metodoPago;
    private String estado;

}
