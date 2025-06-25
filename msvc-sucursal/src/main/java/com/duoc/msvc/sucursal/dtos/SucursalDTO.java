package com.duoc.msvc.sucursal.dtos;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para representar información básica de una sucursal")
public class SucursalDTO {
    @Schema(description = "ID de la sucursal", example = "1")
    private Long id;
    @Schema(description = "Dirección de la sucursal", example = "Av. Providencia 123")
    private String direccion;
    @Schema(description = "Región donde se ubica la sucursal", example = "Metropolitana")
    private String region;
    @Schema(description = "Comuna donde se ubica la sucursal", example = "Providencia")
    private String comuna;
    private Integer cantidadPersonal;
    private String horariosAtencion;
    private List<InventarioDTO> inventarios;
}
