package com.duoc.msvc.sucursal.dtos;

import lombok.*;

import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class SucursalDTO {
    private String direccion;
    private String region;
    private String comuna;
    private String cantidadPersonal;
    private String horariosAtencion;
    private List<InventarioDTO> inventarios;
}
