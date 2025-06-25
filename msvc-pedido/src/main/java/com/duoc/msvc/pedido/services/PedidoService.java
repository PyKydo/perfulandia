package com.duoc.msvc.pedido.services;

import com.duoc.msvc.pedido.dtos.PedidoHateoasDTO;
import com.duoc.msvc.pedido.models.entities.Pedido;
import org.springframework.hateoas.CollectionModel;

public interface PedidoService{
    CollectionModel<PedidoHateoasDTO> findAll();
    CollectionModel<PedidoHateoasDTO> findByIdCliente(Long idCliente);
    String updateEstadoById(Long idPedido, String nuevoEstado);
    PedidoHateoasDTO findById(Long id);
    PedidoHateoasDTO save(Pedido pedido);
    PedidoHateoasDTO convertToDTO(Pedido pedido);
    PedidoHateoasDTO updateById(Long id, Pedido pedido);
    void deleteById(Long id);
}
