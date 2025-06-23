package com.duoc.msvc.pedido.assemblers;

import com.duoc.msvc.pedido.controllers.PedidoController;
import com.duoc.msvc.pedido.dtos.PedidoDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PedidoDTOModelAssembler implements RepresentationModelAssembler<PedidoDTO, EntityModel<PedidoDTO>> {
    @Override
    public EntityModel<PedidoDTO> toModel(PedidoDTO pedido) {
        return EntityModel.of(
                pedido,
                linkTo(methodOn(PedidoController.class).findById(pedido.getId())).withSelfRel()
        );
    }
} 