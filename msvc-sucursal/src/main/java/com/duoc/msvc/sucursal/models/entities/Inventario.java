package com.duoc.msvc.sucursal.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventarios")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Long idInventario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sucursal")
    private Sucursal sucursal;

    @Column(nullable = false)
    private Long idProducto;

    @Column(nullable = false)
    private Integer stock;
}
