package com.duoc.msvc.sucursal.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.List;
import java.util.ArrayList;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para representar información completa de una sucursal")
public class SucursalGetDTO {
    @Schema(description = "ID único de la sucursal", example = "1")
    private Long id;
    @Schema(description = "Dirección de la sucursal", example = "Av. Apoquindo 4501, Local 1201")
    private String direccion;
    @Schema(description = "Región donde se ubica la sucursal", example = "Metropolitana de Santiago")
    private String region;
    @Schema(description = "Comuna donde se ubica la sucursal", example = "Las Condes")
    private String comuna;
    @Schema(description = "Cantidad de personal que trabaja en la sucursal", example = "8")
    private Integer cantidadPersonal;
    @Schema(description = "Horarios de atención de la sucursal", example = "Lunes a Viernes: 10:00 - 20:00, Sábados: 10:00 - 18:00")
    private String horariosAtencion;
    @Schema(description = "Lista de inventarios disponibles en la sucursal")
    private List<InventarioDTO> inventarios;
} 