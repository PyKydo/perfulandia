package com.duoc.msvc.pedido.assemblers;

import com.duoc.msvc.pedido.controllers.PedidoController;
import com.duoc.msvc.pedido.dtos.PedidoDTO;
import com.duoc.msvc.pedido.models.entities.Pedido;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PedidoDTOModelAssembler implements RepresentationModelAssembler<Pedido, PedidoDTO> {
    
    @Override
    public PedidoDTO toModel(Pedido pedido) {
        // La conversión se hace en el servicio, aquí solo agregamos enlaces
        PedidoDTO pedidoDTO = new PedidoDTO();
        // Los datos se establecen en el servicio
        
        // Agregar enlaces HATEOAS
        pedidoDTO.add(linkTo(methodOn(PedidoController.class).findById(pedido.getIdPedido())).withSelfRel());
        pedidoDTO.add(linkTo(methodOn(PedidoController.class).findAll()).withRel("pedidos"));
        pedidoDTO.add(linkTo(methodOn(PedidoController.class).findById(pedido.getIdCliente())).withRel("pedidosPorCliente"));
        pedidoDTO.add(linkTo(methodOn(PedidoController.class).updateEstadoById(pedido.getIdPedido(), "Pagado")).withRel("pagarPedido"));
        pedidoDTO.add(linkTo(methodOn(PedidoController.class).updateEstadoById(pedido.getIdPedido(), "Cancelado")).withRel("cancelarPedido"));
        
        return pedidoDTO;
    }
    
    @Override
    public CollectionModel<PedidoDTO> toCollectionModel(Iterable<? extends Pedido> entities) {
        CollectionModel<PedidoDTO> pedidoDTOs = RepresentationModelAssembler.super.toCollectionModel(entities);
        
        // Agregar enlace a la colección
        pedidoDTOs.add(linkTo(methodOn(PedidoController.class).findAll()).withSelfRel());
        
        return pedidoDTOs;
    }
} 