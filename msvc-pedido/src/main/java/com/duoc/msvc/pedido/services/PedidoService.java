package com.duoc.msvc.pedido.services;

import com.duoc.msvc.pedido.dtos.PedidoGetDTO;
import com.duoc.msvc.pedido.models.entities.Pedido;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import com.duoc.msvc.pedido.dtos.pojos.PedidoClientDTO;

public interface PedidoService{
    CollectionModel<EntityModel<Pedido>> findAll();
    CollectionModel<EntityModel<Pedido>> findByIdCliente(Long idCliente);
    String updateEstadoById(Long idPedido, String nuevoEstado);
    EntityModel<Pedido> findById(Long id);
    EntityModel<Pedido> save(Pedido pedido);
    PedidoGetDTO convertToDTO(Pedido pedido);
    PedidoClientDTO convertToClientDTO(Pedido pedido);
    EntityModel<Pedido> updateById(Long id, Pedido pedido);
    void deleteById(Long id);
}
