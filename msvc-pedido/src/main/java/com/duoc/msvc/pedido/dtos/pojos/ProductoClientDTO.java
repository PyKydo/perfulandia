package com.duoc.msvc.pedido.dtos.pojos;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class ProductoClientDTO {
    private String nombre;
    private String marca;
    private BigDecimal precio;
    private String categoria;
}
