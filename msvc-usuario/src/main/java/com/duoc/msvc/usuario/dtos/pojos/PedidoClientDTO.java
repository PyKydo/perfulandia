package com.duoc.msvc.usuario.dtos.pojos;


import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class PedidoClientDTO {
    @Schema(description = "ID del cliente", example = "1")
    private Long idCliente;
    @Schema(description = "Nombre del cliente", example = "Juan")
    private String nombreCliente;
    @Schema(description = "Apellido del cliente", example = "Pérez")
    private String apellidoCliente;
    @Schema(description = "Dirección de entrega", example = "Av. Libertador 1234")
    private String direccion;
    @Schema(description = "Correo electrónico del cliente", example = "juan.perez@email.com")
    private String correo;
    @Schema(description = "Lista de detalles del pedido")
    private List<DetallePedidoClientDTO> detallesPedido;
    @Schema(description = "Costo del envío", example = "3500.00")
    private BigDecimal costoEnvio;
    @Schema(description = "Total de los detalles", example = "39980.00")
    private BigDecimal totalDetalles;
    @Schema(description = "Monto final del pedido", example = "43480.00")
    private BigDecimal montoFinal;
    @Schema(description = "Método de pago", example = "Tarjeta de Crédito")
    private String metodoPago;
    @Schema(description = "Estado del pedido", example = "Pendiente")
    private String estado;

}
