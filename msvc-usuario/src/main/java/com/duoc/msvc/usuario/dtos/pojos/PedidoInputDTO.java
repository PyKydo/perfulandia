package com.duoc.msvc.usuario.dtos.pojos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para la creación de un pedido desde el microservicio Usuario")
public class PedidoInputDTO {
    
    @Schema(description = "Método de pago seleccionado", example = "Tarjeta de Crédito", required = true)
    @NotNull(message = "El método de pago es obligatorio")
    @NotEmpty(message = "El método de pago no puede estar vacío")
    private String metodoPago;
    
    @Schema(description = "Lista de detalles del pedido (productos y cantidades)", required = true)
    @NotNull(message = "Los detalles del pedido son obligatorios")
    @NotEmpty(message = "Debe incluir al menos un detalle del pedido")
    private List<DetallePedidoInputDTO> detallesPedido;
} 