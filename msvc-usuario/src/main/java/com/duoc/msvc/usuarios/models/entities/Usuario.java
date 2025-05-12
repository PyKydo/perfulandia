package com.duoc.msvc.usuarios.models.entities;


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
    private String nombres;

    @Column(nullable = false)
    @NotBlank(message = "El campo nombre no puede estar vacio")
    private String apellidos;

    private String correo;

    private String telefono;
}
