package com.duoc.msvc.producto.dtos;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Relation(collectionRelation = "productos", itemRelation = "producto")
public class ProductoDTO extends RepresentationModel<ProductoDTO> {
    private Long id;
    private String nombre;
    private String marca;
    private BigDecimal precio;
    private String descripcion;
    private String imagenRepresentativaURL;
    private String categoria;
    private Double porcentajeConcentracion;
}
