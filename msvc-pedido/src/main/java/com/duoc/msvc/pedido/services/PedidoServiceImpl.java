package com.duoc.msvc.pedido.services;

import com.duoc.msvc.pedido.exceptions.PedidoException;
import com.duoc.msvc.pedido.models.entities.Pedido;
import com.duoc.msvc.pedido.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService{
    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public List<Pedido> findAll() {
        return this.pedidoRepository.findAll();
    }

    @Override
    public List<Pedido> findByIdCliente(Long idCliente) {
        return this.pedidoRepository.findByIdCliente(idCliente);
    }

    @Override
    public Pedido findById(Long id) {
        return this.pedidoRepository.findById(id).orElseThrow(
                () -> new PedidoException("El pedido con el id " + id + " no existe en la base de datos")
        );
    }

    @Override
    public Pedido save(Pedido pedido) {
        return this.pedidoRepository.save(pedido);
    }
}
