package com.duoc.msvc.envio.models.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity @Table(name = "envios")
@ToString @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_envio")
    private Long idEnvio;

    @Column(nullable = false)
    @NotBlank(message = "El campo direccion no puede estar vacio")
    private String direccion;

    @Column(nullable = false)
    @NotBlank(message = "El campo ciudad no puede estar vacio")
    private String ciudad;

    @Column(nullable = false)
    @NotBlank(message = "El campo comuna no puede estar vacio")
    private String comuna;

    @Column(nullable = false)
    @NotBlank(message = "El campo region no puede estar vacio")
    private String region;

    @Column(nullable = false)
    @NotBlank(message = "El campo codigo postal no puede estar vacio")
    private String codigoPostal;

    @Column(nullable = false)
    @NotBlank(message = "El campo estado envio no puede estar vacio")
    private String estado;

    @Column(nullable = false)
    @NotNull(message = "El campo estado id pedido no puede estar vacio")
    private Long idPedido;

}
