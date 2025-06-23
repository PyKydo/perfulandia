package com.duoc.msvc.envio.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class EnvioDTO {
    private Long id;
    private Long idPedido;
    private String direccion;
    private String region;
    private BigDecimal costo;
    private String ciudad;
    private String comuna;
    private String codigoPostal;
    private String estado;
}
