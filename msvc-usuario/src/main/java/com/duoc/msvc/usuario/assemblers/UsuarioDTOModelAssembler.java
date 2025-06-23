package com.duoc.msvc.usuario.assemblers;

import com.duoc.msvc.usuario.controllers.UsuarioController;
import com.duoc.msvc.usuario.dtos.UsuarioDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioDTOModelAssembler implements RepresentationModelAssembler<UsuarioDTO, EntityModel<UsuarioDTO>> {
    @Override
    public EntityModel<UsuarioDTO> toModel(UsuarioDTO usuario) {
        return EntityModel.of(
                usuario,
                linkTo(methodOn(UsuarioController.class).findById(usuario.getId())).withSelfRel()
        );
    }
} 