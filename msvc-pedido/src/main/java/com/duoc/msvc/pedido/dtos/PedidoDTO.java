package com.duoc.msvc.pedido.dtos;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Relation(collectionRelation = "pedidos", itemRelation = "pedido")
public class PedidoDTO extends RepresentationModel<PedidoDTO> {
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
