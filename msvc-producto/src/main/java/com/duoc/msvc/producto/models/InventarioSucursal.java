package com.duoc.msvc.producto.models;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class InventarioSucursal {
    private Long idInventarioSucursal;
    private Long idSucursal;
    private Integer cantidad;
}
