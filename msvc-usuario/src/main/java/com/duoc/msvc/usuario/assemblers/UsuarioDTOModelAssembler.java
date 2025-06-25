package com.duoc.msvc.usuario.assemblers;

import com.duoc.msvc.usuario.controllers.UsuarioHateoasController;
import com.duoc.msvc.usuario.dtos.UsuarioHateoasDTO;
import com.duoc.msvc.usuario.models.entities.Usuario;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioDTOModelAssembler implements RepresentationModelAssembler<Usuario, UsuarioHateoasDTO> {

    @Override
    public UsuarioHateoasDTO toModel(Usuario usuario) {
        UsuarioHateoasDTO usuarioDTO = new UsuarioHateoasDTO();
        usuarioDTO.setId(usuario.getIdUsuario());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellido(usuario.getApellido());
        usuarioDTO.setRegion(usuario.getRegion());
        usuarioDTO.setComuna(usuario.getComuna());
        usuarioDTO.setCiudad(usuario.getCiudad());
        usuarioDTO.setCodigoPostal(usuario.getCodigoPostal());
        usuarioDTO.setDireccion(usuario.getDireccion());
        usuarioDTO.setCorreo(usuario.getCorreo());
        usuarioDTO.setContrasena(usuario.getContrasena());
        usuarioDTO.setTelefono(usuario.getTelefono());

        // Agregar enlaces HATEOAS
        usuarioDTO.add(linkTo(methodOn(UsuarioHateoasController.class).findById(usuario.getIdUsuario())).withSelfRel());
        usuarioDTO.add(linkTo(methodOn(UsuarioHateoasController.class).findAll()).withRel("usuarios"));
        usuarioDTO.add(linkTo(methodOn(UsuarioHateoasController.class).obtenerMisPedidos(usuario.getIdUsuario())).withRel("misPedidos"));
        usuarioDTO.add(linkTo(methodOn(UsuarioHateoasController.class).realizarPedido(usuario.getIdUsuario(), null)).withRel("realizarPedido"));

        return usuarioDTO;
    }

    @Override
    public CollectionModel<UsuarioHateoasDTO> toCollectionModel(Iterable<? extends Usuario> entities) {
        CollectionModel<UsuarioHateoasDTO> usuarioDTOs = RepresentationModelAssembler.super.toCollectionModel(entities);
        
        // Agregar enlace a la colección
        usuarioDTOs.add(linkTo(methodOn(UsuarioHateoasController.class).findAll()).withSelfRel());
        
        return usuarioDTOs;
    }
} 