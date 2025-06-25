package com.duoc.msvc.pedido.services;

import com.duoc.msvc.pedido.assemblers.PedidoDTOModelAssembler;
import com.duoc.msvc.pedido.clients.*;
import com.duoc.msvc.pedido.dtos.DetallePedidoDTO;
import com.duoc.msvc.pedido.dtos.PedidoGetDTO;
import com.duoc.msvc.pedido.dtos.PedidoHateoasDTO;
import com.duoc.msvc.pedido.dtos.pojos.*;
import com.duoc.msvc.pedido.exceptions.PedidoException;
import com.duoc.msvc.pedido.models.entities.DetallePedido;
import com.duoc.msvc.pedido.models.entities.Pedido;
import com.duoc.msvc.pedido.repositories.PedidoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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
    @Autowired
    private PagoClient pagoClient;
    @Autowired
    private PedidoDTOModelAssembler assembler;

    @Override
    @Transactional
    public CollectionModel<PedidoHateoasDTO> findAll() {
        List<Pedido> pedidos = this.pedidoRepository.findAllWithDetalles();
        List<PedidoGetDTO> dtos = pedidos.stream().map(this::convertToDTOForCollection).toList();
        return assembler.toCollectionModelFromDTOs(dtos);
    }

    @Override
    @Transactional
    public CollectionModel<PedidoHateoasDTO> findByIdCliente(Long idCliente) {
        List<Pedido> pedidos = this.pedidoRepository.findByIdCliente(idCliente);
        List<PedidoGetDTO> dtos = pedidos.stream().map(this::convertToDTOForCollection).toList();
        return assembler.toCollectionModelFromDTOs(dtos);
    }

    @Override
    public String updateEstadoById(Long idPedido, String nuevoEstado) {
        Pedido pedido = this.pedidoRepository.findById(idPedido).orElseThrow(
                () -> new PedidoException("El pedido con el id " + idPedido + " no existe en la base de datos")
        );

        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            throw new PedidoException("El nuevo estado no puede estar vacío");
        }

        String estadoActual = pedido.getEstado();
        if ("Pagado".equalsIgnoreCase(estadoActual) && "Pagado".equalsIgnoreCase(nuevoEstado)) {
            throw new PedidoException("El pedido ya está pagado");
        }

        try {
            pedido.setEstado(nuevoEstado);
            pedidoRepository.save(pedido);

            if ("Pagado".equalsIgnoreCase(nuevoEstado)) {
                try {
                    String resultado = pagoClient.updateEstadoById(idPedido, "Completado");
                    if (resultado == null || resultado.trim().isEmpty()) {
                        throw new PedidoException("No se recibió respuesta del servicio de pagos");
                    }
                } catch (Exception e) {
                    pedido.setEstado(estadoActual);
                    pedidoRepository.save(pedido);
                    throw new PedidoException("Error al actualizar el estado del pago: " + e.getMessage());
                }
            } else if ("Cancelado".equalsIgnoreCase(nuevoEstado)) {
                try {
                    String resultado = pagoClient.updateEstadoById(idPedido, "Cancelado");
                    if (resultado == null || resultado.trim().isEmpty()) {
                        throw new PedidoException("No se recibió respuesta del servicio de pagos");
                    }
                } catch (Exception e) {
                    pedido.setEstado(estadoActual);
                    pedidoRepository.save(pedido);
                    throw new PedidoException("Error al actualizar el estado del pago: " + e.getMessage());
                }
            }

            return pedido.getEstado();
        } catch (Exception e) {
            pedido.setEstado(estadoActual);
            pedidoRepository.save(pedido);
            throw new PedidoException("Error al actualizar el estado: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public PedidoHateoasDTO updateById(Long id, Pedido pedido) {
        Pedido pedidoDb = pedidoRepository.findById(id).orElseThrow(
            () -> new PedidoException("El pedido con el id " + id + " no existe en la base de datos")
        );

        // Solo se pueden actualizar ciertos campos.
        pedidoDb.setMetodoPago(pedido.getMetodoPago());
        pedidoDb.setEstado(pedido.getEstado());

        return convertToDTO(pedidoRepository.save(pedidoDb));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(
            () -> new PedidoException("El pedido con el id " + id + " no existe en la base de datos")
        );
        // Cambiar estado del pago a Cancelado
        try {
            pagoClient.updateEstadoById(id, "Cancelado");
        } catch (Exception e) {
            // Loguear pero no impedir la eliminación
        }
        // Cambiar estado del envío a Cancelado
        try {
            envioClient.updateEstadoById(id, "Cancelado");
        } catch (Exception e) {
            // Loguear pero no impedir la eliminación
        }
        pedidoRepository.deleteById(id);
    }

    @Override
    public PedidoHateoasDTO findById(Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(
                () -> new PedidoException("El pedido con el id " + id + " no existe en la base de datos")
        );
        return convertToDTO(pedido);
    }

    @Override
    public PedidoHateoasDTO save(Pedido pedido) {
        if(pedido == null){
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }
        if (pedido.getDetallesPedido() == null || pedido.getDetallesPedido().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos un detalle");
        }
        if (pedido.getMetodoPago() == null || pedido.getMetodoPago().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe especificar un método de pago válido");
        }
        BigDecimal totalDetalles = BigDecimal.ZERO;

        for (DetallePedido detalle : pedido.getDetallesPedido()) {
            ProductoClientDTO productoClientDTO = productoClient.findById(detalle.getIdProducto());

            SucursalClientDTO sucursalClientDTO = sucursalClient.getSucursalBestStockByIdProducto(detalle.getIdProducto());

            InventarioClientDTO inventario = sucursalClientDTO.getInventarios().stream()
                    .filter(i -> i.getIdProducto().equals(detalle.getIdProducto()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No se encontró inventario del producto en la sucursal."));

            if (inventario.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto con ID: " + detalle.getIdProducto());
            }

            BigDecimal subtotal = productoClientDTO.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad()));
            totalDetalles = totalDetalles.add(subtotal);

            detalle.setPrecio(productoClientDTO.getPrecio());
            detalle.setIdSucursal(sucursalClientDTO.getId());

            Integer nuevoStock = inventario.getStock() - detalle.getCantidad();

            // Validar que los IDs no sean nulos antes de llamar al servicio
            if (sucursalClientDTO.getId() == null) {
                throw new RuntimeException("ID de sucursal es nulo para el producto: " + detalle.getIdProducto());
            }
            if (inventario.getIdInventario() == null) {
                throw new RuntimeException("ID de inventario es nulo para el producto: " + detalle.getIdProducto());
            }

            sucursalClient.updateStock(
                    sucursalClientDTO.getId(),                  // idSuc
                    inventario.getIdInventario(),              // idInv
                    nuevoStock                                 // nuevoStock
            );
        }

        BigDecimal costoEnvio = envioClient.getCostoEnvio();
        BigDecimal montoFinal = totalDetalles.add(costoEnvio);

        pedido.setCostoEnvio(costoEnvio);

        pedido.setTotalDetalles(totalDetalles);
        pedido.setMontoFinal(montoFinal);

        Pedido saved = pedidoRepository.save(pedido);

        PagoClientDTO pagoClientDTO = new PagoClientDTO();
        pagoClientDTO.setIdPedido(saved.getIdPedido());
        pagoClientDTO.setMonto(montoFinal);
        pagoClientDTO.setMetodoPago(saved.getMetodoPago());

        pagoClient.save(pagoClientDTO);

        EnvioClientDTO envioClientDTO = new EnvioClientDTO();

        envioClientDTO.setIdPedido(saved.getIdPedido());

        UsuarioClientDTO usuarioClientDTO = usuarioClient.getUsuarioById(saved.getIdCliente());

        envioClientDTO.setCiudad(usuarioClientDTO.getCiudad());
        envioClientDTO.setRegion(usuarioClientDTO.getRegion());
        envioClientDTO.setComuna(usuarioClientDTO.getComuna());
        envioClientDTO.setCodigoPostal(usuarioClientDTO.getCodigoPostal());
        envioClientDTO.setDireccion(usuarioClientDTO.getDireccion());
        envioClientDTO.setCosto(pedido.getCostoEnvio());

        envioClient.save(envioClientDTO);

        return convertToDTO(saved);
    }

    @Override
    public PedidoHateoasDTO convertToDTO(Pedido pedido) {
        UsuarioClientDTO usuarioClientDTO = usuarioClient.getUsuarioById(pedido.getIdCliente());
        PedidoGetDTO dto = new PedidoGetDTO();
        dto.setId(pedido.getIdPedido());
        dto.setIdCliente(pedido.getIdCliente());
        dto.setDireccion(usuarioClientDTO.getDireccion());
        dto.setRegion(usuarioClientDTO.getRegion());
        dto.setComuna(usuarioClientDTO.getComuna());
        dto.setNombreCliente(usuarioClientDTO.getNombre());
        dto.setApellidoCliente(usuarioClientDTO.getApellido());
        dto.setCorreo(usuarioClientDTO.getCorreo());
        dto.setCostoEnvio(pedido.getCostoEnvio());
        dto.setTotalDetalles(pedido.getTotalDetalles());
        dto.setMontoFinal(pedido.getMontoFinal());
        dto.setMetodoPago(pedido.getMetodoPago());
        dto.setEstado(pedido.getEstado());
        dto.setFecha(pedido.getFecha());
        List<DetallePedidoDTO> detallesDTO = pedido.getDetallesPedido().stream()
                .map(detalle -> {
                    ProductoClientDTO productoClientDTO = productoClient.findById(detalle.getIdProducto());
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
        return assembler.toModelFromDTO(dto);
    }

    // Nuevo método auxiliar para conversión rápida sin enlaces HATEOAS individuales
    private PedidoGetDTO convertToDTOForCollection(Pedido pedido) {
        UsuarioClientDTO usuarioClientDTO = usuarioClient.getUsuarioById(pedido.getIdCliente());
        PedidoGetDTO dto = new PedidoGetDTO();
        dto.setId(pedido.getIdPedido());
        dto.setIdCliente(pedido.getIdCliente());
        dto.setDireccion(usuarioClientDTO.getDireccion());
        dto.setRegion(usuarioClientDTO.getRegion());
        dto.setComuna(usuarioClientDTO.getComuna());
        dto.setNombreCliente(usuarioClientDTO.getNombre());
        dto.setApellidoCliente(usuarioClientDTO.getApellido());
        dto.setCorreo(usuarioClientDTO.getCorreo());
        dto.setCostoEnvio(pedido.getCostoEnvio());
        dto.setTotalDetalles(pedido.getTotalDetalles());
        dto.setMontoFinal(pedido.getMontoFinal());
        dto.setMetodoPago(pedido.getMetodoPago());
        dto.setEstado(pedido.getEstado());
        dto.setFecha(pedido.getFecha());
        List<DetallePedidoDTO> detallesDTO = pedido.getDetallesPedido().stream()
                .map(detalle -> {
                    ProductoClientDTO productoClientDTO = productoClient.findById(detalle.getIdProducto());
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
