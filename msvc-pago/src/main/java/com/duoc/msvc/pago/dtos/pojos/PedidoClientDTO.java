package com.duoc.msvc.pago.dtos.pojos;


import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class PedidoClientDTO {
    private String nombreCliente;
    private String apellidoCliente;
    private String direccion;
    private String correo;
    private List<DetallePedidoClientDTO> detallesPedido;
    private BigDecimal costoEnvio;
    private BigDecimal totalDetalles;
    private BigDecimal montoFinal;
    private String metodoPago;
    private String estado;

}
