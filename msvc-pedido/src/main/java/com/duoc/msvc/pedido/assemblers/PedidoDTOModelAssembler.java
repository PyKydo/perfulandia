package com.duoc.msvc.pedido.assemblers;

import com.duoc.msvc.pedido.controllers.PedidoHATEOASController;
import com.duoc.msvc.pedido.dtos.PedidoGetDTO;
import com.duoc.msvc.pedido.dtos.PedidoHateoasDTO;
import com.duoc.msvc.pedido.models.entities.Pedido;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PedidoDTOModelAssembler implements RepresentationModelAssembler<Pedido, PedidoHateoasDTO> {
    @Override
    public PedidoHateoasDTO toModel(Pedido pedido) {
        // No se debe usar este método directamente, ya que requiere lógica de negocio externa
        throw new UnsupportedOperationException("Usa toModelFromDTO con un DTO ya poblado");
    }

    @Override
    public CollectionModel<PedidoHateoasDTO> toCollectionModel(Iterable<? extends Pedido> entities) {
        throw new UnsupportedOperationException("Usa toCollectionModelFromDTOs con DTOs ya poblados");
    }

    /**
     * Convierte un PedidoDTO ya poblado a un PedidoHateoasDTO con enlaces HATEOAS
     */
    public PedidoHateoasDTO toModelFromDTO(PedidoGetDTO dto) {
        PedidoHateoasDTO hateoasDTO = new PedidoHateoasDTO();
        hateoasDTO.setIdPedido(dto.getId());
        hateoasDTO.setIdCliente(dto.getIdCliente());
        hateoasDTO.setNombreCliente(dto.getNombreCliente());
        hateoasDTO.setApellidoCliente(dto.getApellidoCliente());
        hateoasDTO.setDireccion(dto.getDireccion());
        hateoasDTO.setCorreo(dto.getCorreo());
        hateoasDTO.setRegion(dto.getRegion());
        hateoasDTO.setComuna(dto.getComuna());
        hateoasDTO.setDetallesPedido(dto.getDetallesPedido());
        hateoasDTO.setCostoEnvio(dto.getCostoEnvio());
        hateoasDTO.setTotalDetalles(dto.getTotalDetalles());
        hateoasDTO.setMontoFinal(dto.getMontoFinal());
        hateoasDTO.setMetodoPago(dto.getMetodoPago());
        hateoasDTO.setEstado(dto.getEstado());
        hateoasDTO.setFecha(dto.getFecha());

        hateoasDTO.add(linkTo(methodOn(PedidoHATEOASController.class).findById(dto.getId())).withSelfRel());
        hateoasDTO.add(linkTo(methodOn(PedidoHATEOASController.class).findAll()).withRel("pedidos"));
        hateoasDTO.add(linkTo(methodOn(PedidoHATEOASController.class).findByIdCliente(dto.getIdCliente())).withRel("pedidosPorCliente"));
        hateoasDTO.add(linkTo(methodOn(PedidoHATEOASController.class).updateEstadoById(dto.getId(), "Pagado")).withRel("pagarPedido"));
        hateoasDTO.add(linkTo(methodOn(PedidoHATEOASController.class).updateEstadoById(dto.getId(), "Cancelado")).withRel("cancelarPedido"));

        return hateoasDTO;
    }


    public CollectionModel<PedidoHateoasDTO> toCollectionModelFromDTOs(Iterable<PedidoGetDTO> dtos) {
        var hateoasList = new java.util.ArrayList<PedidoHateoasDTO>();
        for (PedidoGetDTO dto : dtos) {
            hateoasList.add(toModelFromDTO(dto));
        }
        CollectionModel<PedidoHateoasDTO> collection = CollectionModel.of(hateoasList);
        collection.add(linkTo(methodOn(PedidoHATEOASController.class).findAll()).withSelfRel());
        return collection;
    }
} 