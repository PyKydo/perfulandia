package com.duoc.msvc.pago.assemblers;

import com.duoc.msvc.pago.controllers.PagoController;
import com.duoc.msvc.pago.dtos.PagoDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PagoDTOModelAssembler implements RepresentationModelAssembler<PagoDTO, EntityModel<PagoDTO>> {
    @Override
    public EntityModel<PagoDTO> toModel(PagoDTO pago) {
        return EntityModel.of(
                pago,
                linkTo(methodOn(PagoController.class).findById(pago.getId())).withSelfRel()
        );
    }
} 