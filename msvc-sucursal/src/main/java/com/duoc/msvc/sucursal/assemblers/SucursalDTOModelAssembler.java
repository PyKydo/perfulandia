package com.duoc.msvc.sucursal.assemblers;

import com.duoc.msvc.sucursal.controllers.SucursalController;
import com.duoc.msvc.sucursal.dtos.SucursalDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class SucursalDTOModelAssembler implements RepresentationModelAssembler<SucursalDTO, EntityModel<SucursalDTO>> {
    @Override
    public EntityModel<SucursalDTO> toModel(SucursalDTO sucursal) {
        return EntityModel.of(
                sucursal,
                linkTo(methodOn(SucursalController.class).findById(sucursal.getId())).withSelfRel()
        );
    }
} 