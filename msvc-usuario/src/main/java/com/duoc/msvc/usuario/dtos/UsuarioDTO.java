package com.duoc.msvc.usuario.dtos;

import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class UsuarioDTO {
    private String nombre;
    private String apellido;
    private String ciudad;
    private String direccion;
    private String correo;
    private String rol;
    private String telefono;
}
