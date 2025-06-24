package com.duoc.msvc.pedido.services;

import com.duoc.msvc.pedido.dtos.PedidoDTO;
import com.duoc.msvc.pedido.models.entities.Pedido;
import org.springframework.hateoas.CollectionModel;

import java.util.List;

public interface PedidoService{
    CollectionModel<PedidoDTO> findAll();
    CollectionModel<PedidoDTO> findByIdCliente(Long idCliente);
    String updateEstadoById(Long idPedido, String nuevoEstado);
    PedidoDTO findById(Long id);
    PedidoDTO save(Pedido pedido);
    PedidoDTO convertToDTO(Pedido pedido);
    PedidoDTO updateById(Long id, Pedido pedido);
    void deleteById(Long id);
}
