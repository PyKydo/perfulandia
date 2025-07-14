package com.duoc.msvc.usuario.models.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
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
    private String region;

    @Column(nullable = false)
    private String comuna;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String codigoPostal;

    @Column(nullable = false)
    @Email
    private String correo;

    @Column(nullable = false)
    private String contrasena;
    private String telefono;
}
