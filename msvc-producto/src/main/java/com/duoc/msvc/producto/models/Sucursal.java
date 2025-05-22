package com.duoc.msvc.producto.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sucursales")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sucursal")
    private Long idSucursal;

    private String ciudad;

    private String direccion;



}
