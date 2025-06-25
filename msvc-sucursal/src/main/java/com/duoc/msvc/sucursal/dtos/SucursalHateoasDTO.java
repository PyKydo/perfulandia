package com.duoc.msvc.sucursal.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import java.util.List;
import java.util.ArrayList;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Schema(
    description = "DTO HATEOAS para representar una sucursal con enlaces",
    example = "{\n  \"id\": 1,\n  \"direccion\": \"Av. Principal 1234\",\n  \"region\": \"Metropolitana\",\n  \"comuna\": \"Santiago Centro\",\n  \"cantidadPersonal\": 10,\n  \"horariosAtencion\": \"Lunes a Viernes 09:00-18:00\",\n  \"_links\": {\n    \"self\": {\"href\": \"/api/v1/sucursales/1\"},\n    \"sucursales\": {\"href\": \"/api/v1/sucursales\"}\n  }\n}")
public class SucursalHateoasDTO extends RepresentationModel<SucursalHateoasDTO> {
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