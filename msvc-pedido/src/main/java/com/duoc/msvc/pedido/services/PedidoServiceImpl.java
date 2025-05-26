package com.duoc.msvc.pedido.services;

import com.duoc.msvc.pedido.clients.EnvioClient;
import com.duoc.msvc.pedido.clients.ProductoClient;
import com.duoc.msvc.pedido.clients.SucursalClient;
import com.duoc.msvc.pedido.clients.UsuarioClient;
import com.duoc.msvc.pedido.dtos.DetallePedidoDTO;
import com.duoc.msvc.pedido.dtos.PedidoDTO;
import com.duoc.msvc.pedido.dtos.pojos.*;
import com.duoc.msvc.pedido.exceptions.PedidoException;
import com.duoc.msvc.pedido.models.entities.DetallePedido;
import com.duoc.msvc.pedido.models.entities.Pedido;
import com.duoc.msvc.pedido.repositories.PedidoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService{
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private UsuarioClient usuarioClient;
    @Autowired
    private ProductoClient productoClient;
    @Autowired
    private SucursalClient sucursalClient;
    @Autowired
    private EnvioClient envioClient;

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

    // TODO: Antes de crear un pedido se debe validar que quede stock (stock > 0) y luego de crear un pedido se debe restar la cantidad de producto
    @Override
    public PedidoDTO save(Pedido pedido) {
        BigDecimal total = BigDecimal.ZERO;

        for (DetallePedido detalle : pedido.getDetallesPedido()) {
            ProductoClientDTO productoClientDTO = productoClient.getProductoById(detalle.getIdProducto());

            SucursalClientDTO sucursalClientDTO = sucursalClient.getSucursalBestStockByIdProducto(detalle.getIdProducto());

            InventarioClientDTO inventario = sucursalClientDTO.getInventarios().stream()
                    .filter(i -> i.getIdProducto().equals(detalle.getIdProducto()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No se encontró inventario del producto en la sucursal."));

            if (inventario.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto con ID: " + detalle.getIdProducto());
            }

            BigDecimal subtotal = productoClientDTO.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad()));
            total = total.add(subtotal);

            detalle.setPrecio(productoClientDTO.getPrecio());
            detalle.setIdSucursal(sucursalClientDTO.getIdSucursal());

            int nuevoStock = inventario.getStock() - detalle.getCantidad();
            sucursalClient.updateInventarioStock(
                    sucursalClientDTO.getIdSucursal(),         // idSuc
                    inventario.getIdInventario(),              // idInv
                    nuevoStock                                 // nuevoStock
            );
        }

        pedido.setTotal(total);
        pedido.setCostoEnvio(envioClient.getCostoEnvio());
        Pedido saved = pedidoRepository.save(pedido);

        return convertToDTO(saved);

    }

    @Override
    public PedidoDTO convertToDTO(Pedido pedido) {

        UsuarioClientDTO usuarioClientDTO = usuarioClient.getUsuarioById(pedido.getIdCliente());

        PedidoDTO dto = new PedidoDTO();
        dto.setDireccion(usuarioClientDTO.getDireccion());
        dto.setNombreCliente(usuarioClientDTO.getNombre());
        dto.setApellidoCliente(usuarioClientDTO.getApellido());
        dto.setCorreo(usuarioClientDTO.getCorreo());

        dto.setCostoEnvio(pedido.getCostoEnvio());
        dto.setTotal(pedido.getTotal());
        dto.setEstado(pedido.getEstado());

        // Convertir detalles a DTOs
        List<DetallePedidoDTO> detallesDTO = pedido.getDetallesPedido().stream()
                .map(detalle -> {
                    ProductoClientDTO productoClientDTO = productoClient.getProductoById(detalle.getIdProducto());

                    DetallePedidoDTO detalleDTO = new DetallePedidoDTO();

                    detalleDTO.setIdProducto(detalle.getIdProducto());
                    detalleDTO.setCantidad(detalle.getCantidad());
                    detalleDTO.setPrecio(detalle.getPrecio());
                    detalleDTO.setNombreProducto(productoClientDTO.getNombre());
                    detalleDTO.setMarcaProducto(productoClientDTO.getMarca());

                    return detalleDTO;
                })
                .toList();

        dto.setDetallesPedido(detallesDTO);
        return dto;
    }
}
