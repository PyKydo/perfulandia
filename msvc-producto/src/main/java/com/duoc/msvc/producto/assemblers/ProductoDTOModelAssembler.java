package com.duoc.msvc.producto.assemblers;

import com.duoc.msvc.producto.controllers.ProductoController;
import com.duoc.msvc.producto.dtos.ProductoDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductoDTOModelAssembler implements RepresentationModelAssembler<ProductoDTO, EntityModel<ProductoDTO>> {
    @Override
    public EntityModel<ProductoDTO> toModel(ProductoDTO producto) {
        return EntityModel.of(
                producto,
                linkTo(methodOn(ProductoController.class).findById(producto.getId())).withSelfRel()
        );
    }
} 