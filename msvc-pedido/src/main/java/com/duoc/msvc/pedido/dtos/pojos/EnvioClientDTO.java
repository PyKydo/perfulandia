package com.duoc.msvc.pedido.dtos.pojos;

import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class EnvioClientDTO {
    private String direccion;
    private String ciudad;
    private String comuna;
    private String region;
    private String codigoPostal;
    private String estado;
    private Long idPedido;
}
