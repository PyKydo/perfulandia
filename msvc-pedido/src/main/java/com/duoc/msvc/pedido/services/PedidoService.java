package com.duoc.msvc.pedido.services;


import com.duoc.msvc.pedido.models.entities.Pedido;

import java.util.List;

public interface PedidoService{
    List<Pedido> findAll();
    List<Pedido> findByIdCliente(Long idCliente);
    Pedido findById(Long id);
    Pedido save(Pedido pedido);
}
