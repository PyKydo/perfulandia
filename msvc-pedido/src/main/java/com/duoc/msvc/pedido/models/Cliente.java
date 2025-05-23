package com.duoc.msvc.pedido.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Cliente {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String contrasena;
    private String rol;
    private String correo;
    private String telefono;
}
