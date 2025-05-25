package com.duoc.msvc.pedido.services;

import com.duoc.msvc.pedido.dtos.DetallePedidoDTO;
import com.duoc.msvc.pedido.dtos.PedidoDTO;
import com.duoc.msvc.pedido.exceptions.PedidoException;
import com.duoc.msvc.pedido.models.entities.DetallePedido;
import com.duoc.msvc.pedido.models.entities.Pedido;
import com.duoc.msvc.pedido.repositories.PedidoRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService{
    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public List<PedidoDTO> findAll() {
        return this.pedidoRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<PedidoDTO> findByIdCliente(Long idCliente) {
        return this.pedidoRepository.findByIdCliente(idCliente).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public PedidoDTO findById(Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(
                () -> new PedidoException("El pedido con el id " + id + " no existe en la base de datos")
        );
        return convertToDTO(pedido);
    }

    @Override
    public PedidoDTO save(Pedido pedido) {
        BigDecimal total = BigDecimal.ZERO;

        for(DetallePedido detalle: pedido.getDetallesPedido()) {
            BigDecimal subtotal = detalle.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad()));

        }


        return convertToDTO(this.pedidoRepository.save(pedido));
    }

    @Override
    public PedidoDTO convertToDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setIdCliente(pedido.getIdCliente());
        dto.setTotal(pedido.getTotal());
        dto.setEstado(pedido.getEstado());

        // Convertir detalles a DTOs
        List<DetallePedidoDTO> detallesDTO = pedido.getDetallesPedido().stream()
                .map(detalle -> {
                    DetallePedidoDTO detalleDTO = new DetallePedidoDTO();
                    detalleDTO.setIdProducto(detalle.getIdProducto());
                    detalleDTO.setCantidad(detalle.getCantidad());
                    detalleDTO.setPrecio(detalle.getPrecio());

                    return detalleDTO;
                })
                .toList();

        dto.setDetallesPedido(detallesDTO);
        return dto;
    }
}
