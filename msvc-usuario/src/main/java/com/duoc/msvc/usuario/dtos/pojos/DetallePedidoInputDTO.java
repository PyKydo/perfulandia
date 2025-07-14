package com.duoc.msvc.usuario.dtos.pojos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class DetallePedidoInputDTO {
    
    @Schema(description = "ID del producto a agregar al pedido", example = "5", required = true)
    @NotNull(message = "El ID del producto es obligatorio")
    @Positive(message = "El ID del producto debe ser positivo")
    private Long idProducto;
    
    @Schema(description = "Cantidad del producto a solicitar", example = "1", required = true)
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser positiva")
    private Integer cantidad;
} 