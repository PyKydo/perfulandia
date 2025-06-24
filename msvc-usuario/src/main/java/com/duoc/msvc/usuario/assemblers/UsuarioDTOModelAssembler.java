package com.duoc.msvc.usuario.assemblers;

import com.duoc.msvc.usuario.controllers.UsuarioController;
import com.duoc.msvc.usuario.dtos.UsuarioDTO;
import com.duoc.msvc.usuario.models.entities.Usuario;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioDTOModelAssembler implements RepresentationModelAssembler<Usuario, UsuarioDTO> {

    @Override
    public UsuarioDTO toModel(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getIdUsuario());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellido(usuario.getApellido());
        usuarioDTO.setRegion(usuario.getRegion());
        usuarioDTO.setComuna(usuario.getComuna());
        usuarioDTO.setCiudad(usuario.getCiudad());
        usuarioDTO.setCodigoPostal(usuario.getCodigoPostal());
        usuarioDTO.setDireccion(usuario.getDireccion());
        usuarioDTO.setCorreo(usuario.getCorreo());
        usuarioDTO.setTelefono(usuario.getTelefono());

        // Agregar enlaces HATEOAS
        usuarioDTO.add(linkTo(methodOn(UsuarioController.class).findById(usuario.getIdUsuario())).withSelfRel());
        usuarioDTO.add(linkTo(methodOn(UsuarioController.class).findAll()).withRel("usuarios"));
        usuarioDTO.add(linkTo(methodOn(UsuarioController.class).obtenerMisPedidos(usuario.getIdUsuario())).withRel("misPedidos"));
        usuarioDTO.add(linkTo(methodOn(UsuarioController.class).realizarPedido(usuario.getIdUsuario(), null)).withRel("realizarPedido"));

        return usuarioDTO;
    }

    @Override
    public CollectionModel<UsuarioDTO> toCollectionModel(Iterable<? extends Usuario> entities) {
        CollectionModel<UsuarioDTO> usuarioDTOs = RepresentationModelAssembler.super.toCollectionModel(entities);
        
        // Agregar enlace a la colección
        usuarioDTOs.add(linkTo(methodOn(UsuarioController.class).findAll()).withSelfRel());
        
        return usuarioDTOs;
    }
} 