package com.duoc.msvc.producto.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    @Column(nullable = false)
    @NotBlank(message = "El campo nombre no puede estar vacio")
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "El campo marca no puede estar vacio")
    private String marca;

    @Column(nullable = false)
    @PositiveOrZero
    @NotNull(message = "El campo precio no puede estar vacio")
    private BigDecimal precio; // Precio unitario

    @Size(max = 100)
    private String imagenRepresentativaURL;

    private String descripcion;

    private Double porcentajeConcentracion; // 3% - 30%

    private String categoria = "Sin especificar"; // Citricas, Florales, Orientales, etc.
}
