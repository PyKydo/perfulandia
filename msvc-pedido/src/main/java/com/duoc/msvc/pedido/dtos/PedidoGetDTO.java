package com.duoc.msvc.pedido.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Relation(collectionRelation = "pedidos", itemRelation = "pedido")
@Schema(description = "DTO de respuesta simple para pedidos")
public class PedidoGetDTO extends RepresentationModel<PedidoGetDTO> {
    @Schema(description = "ID del pedido", example = "1")
    private Long id;
    @Schema(description = "ID del usuario que realizó el pedido", example = "10")
    private Long idCliente;
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombreCliente;
    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellidoCliente;
    @Schema(description = "Dirección de entrega", example = "Av. Providencia 123")
    private String direccion;
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@email.com")
    private String correo;
    @Schema(description = "Región de entrega", example = "Metropolitana")
    private String region;
    @Schema(description = "Comuna de entrega", example = "Providencia")
    private String comuna;
    @Schema(description = "Lista de detalles del pedido")
    private List<DetallePedidoDTO> detallesPedido;
    @Schema(description = "Costo de envío", example = "2000")
    private BigDecimal costoEnvio;
    @Schema(description = "Total de los detalles", example = "10000")
    private BigDecimal totalDetalles;
    @Schema(description = "Monto final del pedido", example = "12000")
    private BigDecimal montoFinal;
    @Schema(description = "Método de pago utilizado", example = "Tarjeta de Crédito")
    private String metodoPago;
    @Schema(description = "Estado actual del pedido", example = "Nuevo")
    private String estado;
    @Schema(description = "Fecha de creación del pedido", example = "01/01/2024 12:00:00")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private String fecha;
}
