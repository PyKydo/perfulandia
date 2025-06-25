package com.duoc.msvc.pedido.dtos;

import com.duoc.msvc.pedido.dtos.pojos.DetallePedidoInputDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para la creación de un pedido")
public class PedidoCreateDTO {
    @Schema(description = "ID del usuario que realiza el pedido", example = "1", required = true)
    @NotNull(message = "El ID del cliente es obligatorio")
    @Positive(message = "El ID del cliente debe ser positivo")
    private Long idCliente;
    
    @Schema(description = "Método de pago seleccionado", example = "Tarjeta de Crédito", required = true)
    @NotNull(message = "El método de pago es obligatorio")
    @NotEmpty(message = "El método de pago no puede estar vacío")
    private String metodoPago;
    
    @Schema(description = "Estado inicial del pedido (opcional)", example = "Nuevo")
    private String estado;
    
    @Schema(description = "Lista de detalles del pedido (productos, cantidades, etc.)", required = true)
    @NotNull(message = "Los detalles del pedido son obligatorios")
    @NotEmpty(message = "Debe incluir al menos un detalle del pedido")
    private List<DetallePedidoInputDTO> detallesPedido;
} 