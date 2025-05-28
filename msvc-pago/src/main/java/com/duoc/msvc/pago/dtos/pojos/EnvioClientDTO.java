package com.duoc.msvc.pago.dtos.pojos;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class EnvioClientDTO {
    private BigDecimal costo;
    private String direccion;
    private String ciudad;
    private String comuna;
    private String region;
    private String codigoPostal;
    private String estado;
    private Long idPedido;
}
