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
    @NotBlank(message = "El campo comuna no puede estar vacio")
    private String region;

    @Column(nullable = false)
    @NotBlank(message = "El campo comuna no puede estar vacio")
    private String comuna;

    @Column(nullable = false)
    @NotBlank(message = "El campo ciudad no puede estar vacio")
    private String ciudad;

    @Column(nullable = false)
    @NotBlank(message = "El campo direccion no puede estar vacio")
    private String direccion;

    @Column(nullable = false)
    @NotBlank(message = "El campo codigoPostal no puede estar vacio")
    private String codigoPostal;

    @Column(nullable = false)
    @Email
    @NotBlank(message = "El campo correo no puede estar vacio")
    private String correo;

    @Column(nullable = false)
    @NotBlank(message = "El campo contraseña no puede estar vacio")
    private String contrasena;

    @Digits(integer = 9, fraction = 0)
    private String telefono;
}
