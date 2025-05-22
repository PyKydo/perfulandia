package com.duoc.msvc.producto.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "inventarios_sucursales")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventarioSucursal {
    @Id
    @Column(name = "id_inventario_sucursal")
    private Long idSucursal;

    @Column(nullable = false)
    @NotNull(message = "El campo cantidad no puede estar vacio")
    @Positive
    private Integer cantidad;
}
