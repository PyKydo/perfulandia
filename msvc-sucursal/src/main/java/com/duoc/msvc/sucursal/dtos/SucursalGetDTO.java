package com.duoc.msvc.sucursal.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.List;
import java.util.ArrayList;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para obtener información de una sucursal")
public class SucursalGetDTO {
    @Schema(description = "ID de la sucursal", example = "1")
    private Long id;
    @Schema(description = "Dirección de la sucursal", example = "Av. Principal 1234")
    private String direccion;
    @Schema(description = "Región de la sucursal", example = "Metropolitana")
    private String region;
    @Schema(description = "Comuna de la sucursal", example = "Santiago Centro")
    private String comuna;
    @Schema(description = "Cantidad de personal", example = "10")
    private Integer cantidadPersonal;
    @Schema(description = "Horarios de atención", example = "Lunes a Viernes 09:00-18:00")
    private String horariosAtencion;
    @Schema(description = "Inventarios de la sucursal")
    private List<InventarioDTO> inventarios = new ArrayList<>();
} 