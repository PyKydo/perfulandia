package com.duoc.msvc.sucursal.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventarios_sucursal")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario_sucursal")
    private Long idInventarioSucursal;

    @ManyToOne
    @JoinColumn(name = "id_sucursal")
    private Sucursal sucursal;

    // TODO: Futura conexión de Producto con Sucursal a través de OpenFeign
    @Column(nullable = false)
    private Long idProducto;

    @Column(nullable = false)
    private Integer stock;
}
