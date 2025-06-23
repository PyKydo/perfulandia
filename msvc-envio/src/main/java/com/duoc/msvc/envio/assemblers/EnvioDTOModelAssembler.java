package com.duoc.msvc.envio.assemblers;

import com.duoc.msvc.envio.controllers.EnvioController;
import com.duoc.msvc.envio.dtos.EnvioDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EnvioDTOModelAssembler implements RepresentationModelAssembler<EnvioDTO, EntityModel<EnvioDTO>> {
    @Override
    public EntityModel<EnvioDTO> toModel(EnvioDTO envio) {
        return EntityModel.of(
                envio,
                linkTo(methodOn(EnvioController.class).findById(envio.getId())).withSelfRel()
        );
    }
} 