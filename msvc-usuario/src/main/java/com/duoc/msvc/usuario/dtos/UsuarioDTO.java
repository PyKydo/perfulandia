package com.duoc.msvc.usuario.dtos;

import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String region;
    private String comuna;
    private String ciudad;
    private String codigoPostal;
    private String direccion;
    private String correo;
    private String telefono;
}
