package com.duoc.msvc.pedido.dtos.pojos;

import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class UsuarioClientDTO {
    private Long idCliente;
    private String nombre;
    private String apellido;
    private String correo;
    private String direccion;
    private String ciudad;
    private String comuna;
    private String region;
    private String codigoPostal;
}
