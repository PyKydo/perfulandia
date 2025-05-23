package com.duoc.msvc.pedido.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    @NotNull(message = "El campo idProducto no puede estar vacio")
    private Long idProducto;

    @Column(nullable = false)
    @NotNull(message = "El campo idCliente no puede estar vacio")
    private Long idCliente;

    @Column(nullable = false)
    @Positive
    @NotNull(message = "El campo total no puede estar vacio")
    private BigDecimal total; // Lo mas seguro para el manejo de datos relacionados con el dinero (Y así evitar los problemas de redondeo)

    private String estado = "";

    //@Column(updatable = false)
    //private String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

}
