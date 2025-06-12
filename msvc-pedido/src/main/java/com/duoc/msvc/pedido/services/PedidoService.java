package com.duoc.msvc.pedido.services;


import com.duoc.msvc.pedido.dtos.PedidoDTO;
import com.duoc.msvc.pedido.models.entities.Pedido;

import java.util.List;

public interface PedidoService{
    List<PedidoDTO> findAll();
    List<PedidoDTO> findByIdCliente(Long idCliente);
    String updateEstadoById(Long idPedido, String nuevoEstado);
    PedidoDTO updateById(Long id, Pedido pedido);
    void deleteById(Long id);
    PedidoDTO findById(Long id);
    PedidoDTO save(Pedido pedido);
    PedidoDTO convertToDTO(Pedido pedido);
}
