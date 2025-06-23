package com.duoc.msvc.pedido.models.entities;

import com.duoc.msvc.pedido.models.Producto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "detalles_pedido")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_pedido")
    private Long idDetallePedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido")
    @JsonIgnore
    private Pedido pedido;

    @Column(nullable = false)
    @NotNull(message = "El campo idProducto no puede estar vacio")
    private Long idProducto;

    @Column(nullable = false)
    @NotNull(message = "El campo idSucursal no puede estar vacio")
    private Long idSucursal;

    @Column(nullable = false)
    private Integer cantidad;

    private BigDecimal precio; // Precio historico (al momento de la compra)

    @Override
    public String toString() {
        return "DetallePedido{" +
                "idDetallePedido=" + idDetallePedido +
                ", idProducto=" + idProducto +
                ", idSucursal=" + idSucursal +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                '}';
    }
}
