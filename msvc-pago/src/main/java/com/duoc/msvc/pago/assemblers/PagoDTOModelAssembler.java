package com.duoc.msvc.pago.assemblers;

import com.duoc.msvc.pago.controllers.PagoHateoasController;
import com.duoc.msvc.pago.dtos.PagoHateoasDTO;
import com.duoc.msvc.pago.models.Pago;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PagoDTOModelAssembler implements RepresentationModelAssembler<Pago, PagoHateoasDTO> {
    @Override
    public PagoHateoasDTO toModel(Pago pago) {
        PagoHateoasDTO dto = new PagoHateoasDTO();
        dto.setId(pago.getIdPago());
        dto.setIdPedido(pago.getIdPedido());
        dto.setMonto(pago.getMonto());
        dto.setMetodoPago(pago.getMetodo());
        dto.setEstado(pago.getEstado());
        dto.setFechaPago(pago.getFecha());

        dto.add(linkTo(methodOn(PagoHateoasController.class).findById(pago.getIdPago())).withSelfRel());
        dto.add(linkTo(methodOn(PagoHateoasController.class).findAll()).withRel("pagos"));
        dto.add(linkTo(methodOn(PagoHateoasController.class).findByEstado(pago.getEstado())).withRel("pagosPorEstado"));
        return dto;
    }

    @Override
    public CollectionModel<PagoHateoasDTO> toCollectionModel(Iterable<? extends Pago> entities) {
        CollectionModel<PagoHateoasDTO> dtos = RepresentationModelAssembler.super.toCollectionModel(entities);
        dtos.add(linkTo(methodOn(PagoHateoasController.class).findAll()).withSelfRel());
        return dtos;
    }
} 