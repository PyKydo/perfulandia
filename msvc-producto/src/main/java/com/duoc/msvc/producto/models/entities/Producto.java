package com.duoc.msvc.producto.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @NotBlank(message = "El campo nombre está en blanco")
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "El campo marca está en blanco")
    private String marca;

    @Column(nullable = false)
    @Positive
    @NotNull(message = "El campo precio está en blanco")
    private BigDecimal precio;

    private String descripcion = "Esta es la descripción de un Producto de Perfulandia";

    private String imagenRepresentativaURL = "https://upload.wikimedia.org/wikipedia/commons/a/a3/Image-not-found.png"; // Para guardar el archivo de la imagen (secuencia de bytes).

    private String categoria = "Sin especificar";
}
