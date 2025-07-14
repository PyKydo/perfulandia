package com.duoc.msvc.pedido.dtos.pojos;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class PedidoClientDTO {
    @Schema(description = "ID único del pedido", example = "15")
    private Long idPedido;
    @Schema(description = "ID del cliente", example = "1")
    private Long idCliente;
    @Schema(description = "Nombre del cliente", example = "María Fernanda")
    private String nombreCliente;
    @Schema(description = "Apellido del cliente", example = "González Silva")
    private String apellidoCliente;
    @Schema(description = "Dirección de entrega", example = "Av. Apoquindo 4501, Depto 1201")
    private String direccion;
    @Schema(description = "Correo electrónico del cliente", example = "maria.gonzalez@outlook.com")
    private String correo;
    @Schema(description = "Lista de detalles del pedido")
    private List<DetallePedidoClientDTO> detallesPedido;
    @Schema(description = "Costo del envío", example = "3500.00")
    private BigDecimal costoEnvio;
    @Schema(description = "Total de los detalles", example = "89980.00")
    private BigDecimal totalDetalles;
    @Schema(description = "Monto final del pedido", example = "93480.00")
    private BigDecimal montoFinal;
    @Schema(description = "Método de pago", example = "Tarjeta de Crédito")
    private String metodoPago;
    @Schema(description = "Estado del pedido", example = "Pendiente")
    private String estado;
} 