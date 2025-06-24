package com.duoc.msvc.usuario.dtos;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Relation(collectionRelation = "usuarios", itemRelation = "usuario")
public class UsuarioDTO extends RepresentationModel<UsuarioDTO> {
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
