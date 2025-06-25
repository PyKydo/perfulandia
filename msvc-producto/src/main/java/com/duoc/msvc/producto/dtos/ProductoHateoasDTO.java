package com.duoc.msvc.producto.dtos;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Relation(collectionRelation = "productos", itemRelation = "producto")
@Schema(
    description = "DTO HATEOAS para representar un producto con enlaces",
    example = "{\n  \"id\": 1,\n  \"nombre\": \"Perfume Elegante\",\n  \"marca\": \"Chanel\",\n  \"precio\": 59990.00,\n  \"descripcion\": \"Perfume elegante con notas de madera y especias\",\n  \"imagenRepresentativaURL\": \"https://ejemplo.com/imagen.jpg\",\n  \"categoria\": \"Perfumes\",\n  \"porcentajeConcentracion\": 85.5,\n  \"_links\": {\n    \"self\": {\"href\": \"/api/v1/productos/1\"},\n    \"productos\": {\"href\": \"/api/v1/productos\"}\n  }\n}")
public class ProductoHateoasDTO extends RepresentationModel<ProductoHateoasDTO> {
    @Schema(description = "ID del producto", example = "1")
    private Long id;
    @Schema(description = "Nombre del producto", example = "Perfume Elegante")
    private String nombre;
    @Schema(description = "Marca del producto", example = "Chanel")
    private String marca;
    @Schema(description = "Precio del producto", example = "59990.00")
    private BigDecimal precio;
    @Schema(description = "Descripción del producto", example = "Perfume elegante con notas de madera y especias")
    private String descripcion;
    @Schema(description = "URL de la imagen representativa", example = "https://ejemplo.com/imagen.jpg")
    private String imagenRepresentativaURL;
    @Schema(description = "Categoría del producto", example = "Perfumes")
    private String categoria;
    @Schema(description = "Porcentaje de concentración de la fragancia", example = "85.5")
    private Double porcentajeConcentracion;
} 