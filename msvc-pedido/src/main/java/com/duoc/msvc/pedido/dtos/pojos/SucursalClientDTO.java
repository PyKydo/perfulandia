package com.duoc.msvc.pedido.dtos.pojos;

import lombok.*;

import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class SucursalClientDTO {
    private Long idSucursal;
    private String direccion;
    private String region;
    private String comuna;
    private Integer cantidadPersonal;
    private String horariosAtencion;
    private List<InventarioClientDTO> inventarios;
}
