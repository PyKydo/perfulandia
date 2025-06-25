package com.duoc.msvc.envio.assemblers;

import com.duoc.msvc.envio.controllers.EnvioHateoasController;
import com.duoc.msvc.envio.dtos.EnvioHateoasDTO;
import com.duoc.msvc.envio.models.entities.Envio;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EnvioDTOModelAssembler implements RepresentationModelAssembler<Envio, EnvioHateoasDTO> {
    @Override
    public EnvioHateoasDTO toModel(Envio envio) {
        EnvioHateoasDTO dto = new EnvioHateoasDTO();
        dto.setId(envio.getIdEnvio());
        dto.setIdPedido(envio.getIdPedido());
        dto.setDireccion(envio.getDireccion());
        dto.setRegion(envio.getRegion());
        dto.setCiudad(envio.getCiudad());
        dto.setComuna(envio.getComuna());
        dto.setCodigoPostal(envio.getCodigoPostal());
        dto.setCosto(envio.getCosto());
        dto.setEstado(envio.getEstado());
        dto.setFechaEstimadaEntrega(envio.getFechaEstimadaEntrega());
        dto.add(linkTo(methodOn(EnvioHateoasController.class).findById(envio.getIdEnvio())).withSelfRel());
        dto.add(linkTo(methodOn(EnvioHateoasController.class).findAll()).withRel("envios"));
        return dto;
    }
    @Override
    public CollectionModel<EnvioHateoasDTO> toCollectionModel(Iterable<? extends Envio> entities) {
        CollectionModel<EnvioHateoasDTO> dtos = RepresentationModelAssembler.super.toCollectionModel(entities);
        dtos.add(linkTo(methodOn(EnvioHateoasController.class).findAll()).withSelfRel());
        return dtos;
    }
} 