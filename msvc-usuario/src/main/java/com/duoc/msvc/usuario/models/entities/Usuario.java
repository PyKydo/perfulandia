package com.duoc.msvc.usuario.models.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false)
    @NotBlank(message = "El campo nombre no puede estar vacio")
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "El campo apellido no puede estar vacio")
    private String apellido;

    @Column(nullable = false)
    @NotBlank(message = "El campo correo no puede estar vacio")
    private String correo;

    @Column(nullable = false)
    @NotBlank(message = "El campo contraseña no puede estar vacio")
    private String contrasena;

    @Column(nullable = false)
    @NotBlank(message = "El campo rol no puede estar vacio")
    private String rol;

    private String telefono;
}
