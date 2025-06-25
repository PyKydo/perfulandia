package com.duoc.msvc.pedido.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para la actualización de un pedido")
public class PedidoUpdateDTO {
    
    @Schema(description = "Método de pago actualizado", example = "Transferencia Bancaria")
    @NotEmpty(message = "El método de pago no puede estar vacío")
    private String metodoPago;
    
    @Schema(description = "Nuevo estado del pedido", example = "En Proceso")
    @NotEmpty(message = "El estado no puede estar vacío")
    private String estado;
} 