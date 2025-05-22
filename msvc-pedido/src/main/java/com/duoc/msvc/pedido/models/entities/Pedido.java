package com.duoc.msvc.pedido.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @NotNull(message = "El campo idProducto no puede estar vacio")
    private Long idProducto;

    @Column(nullable = false)
    @NotNull(message = "El campo idCliente no puede estar vacio")
    private Long idCliente;

    @Column(nullable = false)
    @NotBlank(message = "El campo fecha no puede estar vacio")
    private LocalDateTime fecha;

    @Column(nullable = false)
    @NotBlank(message = "El campo estado no puede estar vacio")
    private String estado;

    @Column(nullable = false)
    @Positive
    @NotBlank(message = "El campo total no puede estar vacio")
    private Integer total;

}
