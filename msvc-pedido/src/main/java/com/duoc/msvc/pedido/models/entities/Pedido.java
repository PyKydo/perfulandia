package com.duoc.msvc.pedido.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long idPedido;

    @Column(updatable = false) // Para que no se pueda cambiar la fecha
    private String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")); // Fecha de la realización del pedido

    @Column(name = "costo_envio")
    private BigDecimal costoEnvio = BigDecimal.ZERO;

    private String metodoPago;

    @Column(name = "total_detalles")
    private BigDecimal totalDetalles = BigDecimal.ZERO;

    @Column(name = "monto_final")
    private BigDecimal montoFinal = BigDecimal.ZERO;

    @Column(nullable = false)
    private String estado = "Nuevo"; // Nuevo, Pagado, Enviado, Cancelado

    @Column(nullable = false)
    @NotNull(message = "El campo idCliente no puede estar vacio")
    private Long idCliente;

    @Column(nullable = false)
    @OneToMany(mappedBy = "pedido",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detallesPedido = new ArrayList<>();

    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", fecha='" + fecha + '\'' +
                ", costoEnvio=" + costoEnvio +
                ", metodoPago='" + metodoPago + '\'' +
                ", totalDetalles=" + totalDetalles +
                ", montoFinal=" + montoFinal +
                ", estado='" + estado + '\'' +
                ", idCliente=" + idCliente +
                ", detallesPedido.size=" + (detallesPedido != null ? detallesPedido.size() : 0) +
                '}';
    }
}
