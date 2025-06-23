package com.duoc.msvc.producto.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String marca;
    private BigDecimal precio;
    private String descripcion;
    private String imagenRepresentativaURL;
    private String categoria;
    private Double porcentajeConcentracion;
}
