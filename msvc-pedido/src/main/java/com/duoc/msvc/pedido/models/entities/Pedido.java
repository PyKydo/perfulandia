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

    @Column(updatable = false) // Para que no se pueda cambiar la fecha
    private String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")); // Fecha del pedido

    @PositiveOrZero
    private BigDecimal total; // Dato calculado: sumar todos los detalles (cantidad * precio)
    // Lo mas seguro para el manejo de datos relacionados con el dinero (Y así evitar los problemas de redondeo)

    @Column(nullable = false)
    private String estado = "En proceso"; // En proceso, Pagado, Enviado

    @Column(nullable = false)
    @NotNull(message = "El campo idCliente no puede estar vacio")
    private Long idCliente;

    @Column(nullable = false)
    @OneToMany(mappedBy = "pedido",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detallesPedido = new ArrayList<>();





}
