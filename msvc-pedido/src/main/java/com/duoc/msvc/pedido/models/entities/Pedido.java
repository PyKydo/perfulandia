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
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long idPedido;

    @Column(nullable = false)
    @OneToMany(mappedBy = "pedido",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detallesPedido = new ArrayList<>();

    @Column(nullable = false)
    @NotNull(message = "El campo idCliente no puede estar vacio")
    private Long idCliente;

    @Column(nullable = false)
    @PositiveOrZero
    @NotNull(message = "El campo total no puede estar vacio")
    private BigDecimal total; // Lo mas seguro para el manejo de datos relacionados con el dinero (Y así evitar los problemas de redondeo)

    private String estado = "En proceso"; // En proceso, Pagado, Enviado

    @Column(updatable = false)
    private String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")); // Fecha del pedido;

    public void agregarDetalle(DetallePedido detalle) {
        detallesPedido.add(detalle);
        detalle.setPedido(this);
    }
}
