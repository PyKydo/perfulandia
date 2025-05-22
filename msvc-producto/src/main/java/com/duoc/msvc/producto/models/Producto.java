package com.duoc.msvc.producto.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Blob;

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
    private String descripcion;

    @Column(nullable = false)
    private Blob imagenRepresentativa;

    @Column(nullable = false)
    @NotBlank(message = "El campo precio está en blanco")
    private Integer precio;

    @Column(nullable = false)
    private String categoria;
}
