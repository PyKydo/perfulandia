package com.duoc.msvc.pago.dtos;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import java.util.Map;

@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "DTO para representar errores en la API de pagos")
public class ErrorDTO {
    @Schema(description = "Código de estado HTTP", example = "400")
    private Integer status;
    @Schema(description = "Fecha y hora del error", example = "2024-03-20T15:30:00.000Z")
    private Date date;
    @Schema(description = "Mapa de errores con campo y mensaje", example = "{\"monto\":\"es obligatorio\",\"metodoPago\":\"es obligatorio\"}")
    private Map<String, String> errors;

    @Override
    public String toString() {
        return "{" +
                "status=" + status +
                ", date=" + date +
                ", errors=" + errors +
                '}';
    }
}
