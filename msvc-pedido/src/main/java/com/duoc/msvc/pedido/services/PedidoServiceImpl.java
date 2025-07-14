package com.duoc.msvc.pedido.services;

import com.duoc.msvc.pedido.clients.*;
import com.duoc.msvc.pedido.dtos.DetallePedidoDTO;
import com.duoc.msvc.pedido.dtos.PedidoGetDTO;
import com.duoc.msvc.pedido.dtos.pojos.*;
import com.duoc.msvc.pedido.exceptions.PedidoException;
import com.duoc.msvc.pedido.models.entities.DetallePedido;
import com.duoc.msvc.pedido.models.entities.Pedido;
import com.duoc.msvc.pedido.repositories.PedidoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import com.duoc.msvc.pedido.controllers.PedidoHateoasController;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService{
    
    private static final Logger logger = LoggerFactory.getLogger(PedidoServiceImpl.class);
    
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
    
    @Value("${msvc.usuario.url}")
    private String usuarioUrl;
    
    @Value("${msvc.producto.url}")
    private String productoUrl;
    @Value("${msvc.sucursal.url}")
    private String sucursalUrl;

    @Override
    @Transactional
    public CollectionModel<EntityModel<Pedido>> findAll() {
        logger.info("Iniciando búsqueda de todos los pedidos");
        List<Pedido> pedidos = this.pedidoRepository.findAllWithDetalles();
        List<EntityModel<Pedido>> entityModels = pedidos.stream()
            .map(this::toEntityModel)
            .toList();
        logger.info("Búsqueda completada. Se encontraron {} pedidos", pedidos.size());
        return CollectionModel.of(entityModels,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoHateoasController.class).findAll()).withSelfRel()
        );
    }

    @Override
    @Transactional
    public CollectionModel<EntityModel<Pedido>> findByIdCliente(Long idCliente) {
        logger.info("Buscando pedidos del cliente ID: {}", idCliente);
        List<Pedido> pedidos = this.pedidoRepository.findByIdCliente(idCliente);
        List<EntityModel<Pedido>> entityModels = pedidos.stream()
            .map(this::toEntityModel)
            .toList();
        logger.info("Se encontraron {} pedidos para el cliente ID: {}", pedidos.size(), idCliente);
        return CollectionModel.of(entityModels,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoHateoasController.class).findByIdCliente(idCliente)).withSelfRel()
        );
    }

    @Override
    public String updateEstadoById(Long idPedido, String nuevoEstado) {
        logger.info("Iniciando actualización de estado. Pedido ID: {}, Nuevo estado: {}", idPedido, nuevoEstado);
        
        Pedido pedido = this.pedidoRepository.findById(idPedido).orElseThrow(
                () -> new PedidoException("El pedido con el id " + idPedido + " no existe en la base de datos")
        );

        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            logger.error("Error: El nuevo estado no puede estar vacío");
            throw new PedidoException("El nuevo estado no puede estar vacío");
        }

        String estadoActual = pedido.getEstado();
        if ("Pagado".equalsIgnoreCase(estadoActual) && "Pagado".equalsIgnoreCase(nuevoEstado)) {
            logger.error("Error: El pedido ya está pagado. Estado actual: {}", estadoActual);
            throw new PedidoException("El pedido ya está pagado");
        }

        try {
            logger.info("Actualizando estado del pedido de '{}' a '{}'", estadoActual, nuevoEstado);
            pedido.setEstado(nuevoEstado);
            pedidoRepository.save(pedido);

            if ("Pagado".equalsIgnoreCase(nuevoEstado)) {
                logger.info("Actualizando estado del pago a 'Completado'");
                try {
                    String resultado = pagoClient.updateEstadoById(idPedido, "Completado");
                    if (resultado == null || resultado.trim().isEmpty()) {
                        logger.error("Error: No se recibió respuesta del servicio de pagos");
                        throw new PedidoException("No se recibió respuesta del servicio de pagos");
                    }
                    logger.info("Estado del pago actualizado exitosamente");
                } catch (Exception e) {
                    logger.error("Error al actualizar estado del pago: {}", e.getMessage(), e);
                    pedido.setEstado(estadoActual);
                    pedidoRepository.save(pedido);
                    throw new PedidoException("Error al actualizar el estado del pago: " + e.getMessage());
                }
            } else if ("Cancelado".equalsIgnoreCase(nuevoEstado)) {
                logger.info("Actualizando estado del pago a 'Cancelado'");
                try {
                    String resultado = pagoClient.updateEstadoById(idPedido, "Cancelado");
                    if (resultado == null || resultado.trim().isEmpty()) {
                        logger.error("Error: No se recibió respuesta del servicio de pagos");
                        throw new PedidoException("No se recibió respuesta del servicio de pagos");
                    }
                    logger.info("Estado del pago cancelado exitosamente");
                } catch (Exception e) {
                    logger.error("Error al cancelar estado del pago: {}", e.getMessage(), e);
                    pedido.setEstado(estadoActual);
                    pedidoRepository.save(pedido);
                    throw new PedidoException("Error al actualizar el estado del pago: " + e.getMessage());
                }
            }

            logger.info("Estado del pedido actualizado exitosamente a: {}", pedido.getEstado());
            return pedido.getEstado();
        } catch (Exception e) {
            logger.error("Error al actualizar el estado: {}", e.getMessage(), e);
            pedido.setEstado(estadoActual);
            pedidoRepository.save(pedido);
            throw new PedidoException("Error al actualizar el estado: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public EntityModel<Pedido> updateById(Long id, Pedido pedidoActualizado) {
        logger.info("Iniciando actualización de pedido con ID: {}", id);
        
        Pedido pedidoDb = pedidoRepository.findById(id).orElseThrow(
            () -> new PedidoException("El pedido con el id " + id + " no existe en la base de datos")
        );

        logger.info("Actualizando método de pago de '{}' a '{}'", pedidoDb.getMetodoPago(), pedidoActualizado.getMetodoPago());
        logger.info("Actualizando estado de '{}' a '{}'", pedidoDb.getEstado(), pedidoActualizado.getEstado());
        
        pedidoDb.setMetodoPago(pedidoActualizado.getMetodoPago());
        pedidoDb.setEstado(pedidoActualizado.getEstado());

        Pedido pedidoGuardado = pedidoRepository.save(pedidoDb);
        logger.info("Pedido actualizado exitosamente");
        return toEntityModel(pedidoGuardado);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        logger.info("Iniciando eliminación de pedido con ID: {}", id);
        
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(
            () -> new PedidoException("El pedido con el id " + id + " no existe en la base de datos")
        );
        
        logger.info("Cancelando pago asociado al pedido");
        try {
            pagoClient.updateEstadoById(id, "Cancelado");
            logger.info("Pago cancelado exitosamente");
        } catch (Exception e) {
            logger.warn("No se pudo cancelar el pago: {}", e.getMessage());
        }
        
        logger.info("Cancelando envío asociado al pedido");
        try {
            envioClient.updateEstadoById(id, "Cancelado");
            logger.info("Envío cancelado exitosamente");
        } catch (Exception e) {
            logger.warn("No se pudo cancelar el envío: {}", e.getMessage());
        }
        
        pedidoRepository.deleteById(id);
        logger.info("Pedido eliminado exitosamente");
    }

    @Override
    public EntityModel<Pedido> findById(Long id) {
        logger.info("Buscando pedido con ID: {}", id);
        
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(
                () -> new PedidoException("El pedido con el id " + id + " no existe en la base de datos")
        );
        
        logger.info("Pedido encontrado. Cliente ID: {}, Estado: {}", pedido.getIdCliente(), pedido.getEstado());
        return toEntityModel(pedido);
    }

    @Override
    public EntityModel<Pedido> save(Pedido pedido) {
        if(pedido == null){
            logger.error("Error: El pedido no puede ser nulo");
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }
        logger.info("Iniciando creación de pedido para cliente ID: {}", pedido.getIdCliente());
        
        if (pedido.getDetallesPedido() == null || pedido.getDetallesPedido().isEmpty()) {
            logger.error("Error: El pedido debe contener al menos un detalle");
            throw new IllegalArgumentException("El pedido debe contener al menos un detalle");
        }
        if (pedido.getMetodoPago() == null || pedido.getMetodoPago().trim().isEmpty()) {
            logger.error("Error: Debe especificar un método de pago válido");
            throw new IllegalArgumentException("Debe especificar un método de pago válido");
        }
        
        logger.info("Procesando {} detalles de pedido", pedido.getDetallesPedido().size());
        BigDecimal totalDetalles = BigDecimal.ZERO;

        for (DetallePedido detalle : pedido.getDetallesPedido()) {
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                logger.error("Error: La cantidad debe ser mayor a 0 para el producto ID: {}", detalle.getIdProducto());
                throw new PedidoException("La cantidad debe ser mayor a 0");
            }
            logger.info("Procesando detalle - Producto ID: {}, Cantidad: {}", detalle.getIdProducto(), detalle.getCantidad());
            
            ProductoClientDTO productoClientDTO = productoClient.findById(detalle.getIdProducto());
            if (productoClientDTO == null) {
                logger.error("Error: No se encontró el producto con ID: {}", detalle.getIdProducto());
                throw new PedidoException("No se encontró el producto con ID: " + detalle.getIdProducto());
            }
            logger.info("Producto encontrado: {} - Precio: {}", productoClientDTO.getNombre(), productoClientDTO.getPrecio());

            SucursalClientDTO sucursalClientDTO = sucursalClient.getSucursalBestStockByIdProducto(detalle.getIdProducto());
            logger.info("Sucursal seleccionada: {} - ID: {}", sucursalClientDTO.getDireccion(), sucursalClientDTO.getId());

            InventarioClientDTO inventario = sucursalClientDTO.getInventarios().stream()
                    .filter(i -> i.getIdProducto().equals(detalle.getIdProducto()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No se encontró inventario del producto en la sucursal."));

            logger.info("Stock disponible: {}, Cantidad solicitada: {}", inventario.getStock(), detalle.getCantidad());
            
            if (inventario.getStock() < detalle.getCantidad()) {
                logger.error("Error: Stock insuficiente para el producto ID: {}. Disponible: {}, Solicitado: {}", 
                           detalle.getIdProducto(), inventario.getStock(), detalle.getCantidad());
                throw new RuntimeException("Stock insuficiente para el producto con ID: " + detalle.getIdProducto());
            }

            BigDecimal subtotal = productoClientDTO.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad()));
            totalDetalles = totalDetalles.add(subtotal);
            logger.info("Subtotal del detalle: {}", subtotal);

            detalle.setPrecio(productoClientDTO.getPrecio());
            detalle.setIdSucursal(sucursalClientDTO.getId());

            Integer nuevoStock = inventario.getStock() - detalle.getCantidad();
            logger.info("Actualizando stock de {} a {}", inventario.getStock(), nuevoStock);

            if (sucursalClientDTO.getId() == null) {
                logger.error("Error: ID de sucursal es nulo para el producto: {}", detalle.getIdProducto());
                throw new RuntimeException("ID de sucursal es nulo para el producto: " + detalle.getIdProducto());
            }
            if (inventario.getIdInventario() == null) {
                logger.error("Error: ID de inventario es nulo para el producto: {}", detalle.getIdProducto());
                throw new RuntimeException("ID de inventario es nulo para el producto: " + detalle.getIdProducto());
            }

            sucursalClient.updateStock(
                    sucursalClientDTO.getId(),
                    inventario.getIdInventario(),
                    nuevoStock
            );
            logger.info("Stock actualizado exitosamente");
        }

        logger.info("Calculando costo de envío");
        BigDecimal costoEnvio = envioClient.getCostoEnvio();
        BigDecimal montoFinal = totalDetalles.add(costoEnvio);
        
        logger.info("Totales calculados - Detalles: {}, Envío: {}, Final: {}", totalDetalles, costoEnvio, montoFinal);

        pedido.setCostoEnvio(costoEnvio);
        pedido.setTotalDetalles(totalDetalles);
        pedido.setMontoFinal(montoFinal);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        logger.info("Pedido guardado exitosamente con ID: {}", pedidoGuardado.getIdPedido());

        logger.info("Creando pago asociado");
        PagoClientDTO pagoClientDTO = new PagoClientDTO();
        pagoClientDTO.setIdPedido(pedidoGuardado.getIdPedido());
        pagoClientDTO.setMonto(montoFinal);
        pagoClientDTO.setMetodoPago(pedidoGuardado.getMetodoPago());

        pagoClient.save(pagoClientDTO);
        logger.info("Pago creado exitosamente");

        logger.info("Creando envío asociado");
        EnvioClientDTO envioClientDTO = new EnvioClientDTO();
        envioClientDTO.setIdPedido(pedidoGuardado.getIdPedido());

        UsuarioClientDTO usuarioClientDTO = usuarioClient.getUsuarioById(pedidoGuardado.getIdCliente());
        logger.info("Datos de envío obtenidos del cliente: {} {}", usuarioClientDTO.getNombre(), usuarioClientDTO.getApellido());

        envioClientDTO.setCiudad(usuarioClientDTO.getCiudad());
        envioClientDTO.setRegion(usuarioClientDTO.getRegion());
        envioClientDTO.setComuna(usuarioClientDTO.getComuna());
        envioClientDTO.setCodigoPostal(usuarioClientDTO.getCodigoPostal());
        envioClientDTO.setDireccion(usuarioClientDTO.getDireccion());
        envioClientDTO.setCosto(pedido.getCostoEnvio());

        envioClient.save(envioClientDTO);
        logger.info("Envío creado exitosamente");

        logger.info("Proceso de creación de pedido completado exitosamente");
        return toEntityModel(pedidoGuardado);
    }

    @Override
    public PedidoGetDTO convertToDTO(Pedido pedido) {
        logger.info("Convirtiendo pedido ID: {} a DTO", pedido.getIdPedido());
        
        PedidoGetDTO dto = new PedidoGetDTO();
        dto.setId(pedido.getIdPedido());
        dto.setIdCliente(pedido.getIdCliente());
        dto.setCostoEnvio(pedido.getCostoEnvio());
        dto.setTotalDetalles(pedido.getTotalDetalles());
        dto.setMontoFinal(pedido.getMontoFinal());
        dto.setMetodoPago(pedido.getMetodoPago());
        dto.setEstado(pedido.getEstado());
        dto.setFecha(pedido.getFecha());
        
        logger.info("Obteniendo información del cliente ID: {}", pedido.getIdCliente());
        try {
            UsuarioClientDTO usuarioClientDTO = usuarioClient.getUsuarioById(pedido.getIdCliente());
            dto.setDireccion(usuarioClientDTO.getDireccion());
            dto.setRegion(usuarioClientDTO.getRegion());
            dto.setComuna(usuarioClientDTO.getComuna());
            dto.setNombreCliente(usuarioClientDTO.getNombre());
            dto.setApellidoCliente(usuarioClientDTO.getApellido());
            dto.setCorreo(usuarioClientDTO.getCorreo());
            logger.info("Información del cliente obtenida: {} {}", usuarioClientDTO.getNombre(), usuarioClientDTO.getApellido());
        } catch (Exception e) {
            logger.warn("No se pudo obtener información del cliente: {}", e.getMessage());
            dto.setNombreCliente("Cliente no encontrado");
            dto.setApellidoCliente("");
            dto.setDireccion("");
            dto.setCorreo("");
            dto.setRegion("");
            dto.setComuna("");
        }
        
        logger.info("Procesando {} detalles del pedido", pedido.getDetallesPedido().size());
        List<DetallePedidoDTO> detallesDTO = pedido.getDetallesPedido().stream()
                .map(detalle -> {
                    DetallePedidoDTO detalleDTO = new DetallePedidoDTO();
                    detalleDTO.setIdProducto(detalle.getIdProducto());
                    detalleDTO.setCantidad(detalle.getCantidad());
                    detalleDTO.setPrecio(detalle.getPrecio());
                    
                    logger.info("Obteniendo información del producto ID: {}", detalle.getIdProducto());
                    try {
                        ProductoClientDTO productoClientDTO = productoClient.findById(detalle.getIdProducto());
                        detalleDTO.setNombreProducto(productoClientDTO.getNombre());
                        detalleDTO.setMarcaProducto(productoClientDTO.getMarca());
                        logger.info("Información del producto obtenida: {} - {}", productoClientDTO.getNombre(), productoClientDTO.getMarca());
                    } catch (Exception e) {
                        logger.warn("No se pudo obtener información del producto ID {}: {}", detalle.getIdProducto(), e.getMessage());
                        detalleDTO.setNombreProducto("Producto no encontrado");
                        detalleDTO.setMarcaProducto("");
                    }
                    
                    return detalleDTO;
                })
                .toList();
        dto.setDetallesPedido(detallesDTO);
        
        logger.info("Conversión a DTO completada exitosamente");
        return dto;
    }

    @Override
    public PedidoClientDTO convertToClientDTO(Pedido pedido) {
        logger.info("Convirtiendo pedido ID: {} a PedidoClientDTO", pedido.getIdPedido());
        
        PedidoClientDTO dto = new PedidoClientDTO();
        dto.setIdPedido(pedido.getIdPedido());
        dto.setIdCliente(pedido.getIdCliente());
        dto.setCostoEnvio(pedido.getCostoEnvio());
        dto.setTotalDetalles(pedido.getTotalDetalles());
        dto.setMontoFinal(pedido.getMontoFinal());
        dto.setMetodoPago(pedido.getMetodoPago());
        dto.setEstado(pedido.getEstado());
        
        logger.info("Obteniendo información del cliente ID: {}", pedido.getIdCliente());
        try {
            UsuarioClientDTO usuarioClientDTO = usuarioClient.getUsuarioById(pedido.getIdCliente());
            dto.setDireccion(usuarioClientDTO.getDireccion());
            dto.setNombreCliente(usuarioClientDTO.getNombre());
            dto.setApellidoCliente(usuarioClientDTO.getApellido());
            dto.setCorreo(usuarioClientDTO.getCorreo());
            logger.info("Información del cliente obtenida: {} {}", usuarioClientDTO.getNombre(), usuarioClientDTO.getApellido());
        } catch (Exception e) {
            logger.warn("No se pudo obtener información del cliente: {}", e.getMessage());
            dto.setNombreCliente("Cliente no encontrado");
            dto.setApellidoCliente("");
            dto.setDireccion("");
            dto.setCorreo("");
        }
        
        logger.info("Procesando {} detalles del pedido", pedido.getDetallesPedido().size());
        List<DetallePedidoClientDTO> detallesDTO = pedido.getDetallesPedido().stream()
                .map(detalle -> {
                    DetallePedidoClientDTO detalleDTO = new DetallePedidoClientDTO();
                    detalleDTO.setIdProducto(detalle.getIdProducto());
                    detalleDTO.setCantidad(detalle.getCantidad());
                    detalleDTO.setPrecio(detalle.getPrecio());
                    
                    logger.info("Obteniendo información del producto ID: {}", detalle.getIdProducto());
                    try {
                        ProductoClientDTO productoClientDTO = productoClient.findById(detalle.getIdProducto());
                        detalleDTO.setNombreProducto(productoClientDTO.getNombre());
                        detalleDTO.setMarcaProducto(productoClientDTO.getMarca());
                        logger.info("Información del producto obtenida: {} - {}", productoClientDTO.getNombre(), productoClientDTO.getMarca());
                    } catch (Exception e) {
                        logger.warn("No se pudo obtener información del producto ID {}: {}", detalle.getIdProducto(), e.getMessage());
                        detalleDTO.setNombreProducto("Producto no encontrado");
                        detalleDTO.setMarcaProducto("");
                    }
                    
                    return detalleDTO;
                })
                .toList();
        dto.setDetallesPedido(detallesDTO);
        
        logger.info("Conversión a PedidoClientDTO completada exitosamente");
        return dto;
    }

    private EntityModel<Pedido> toEntityModel(Pedido pedido) {
        EntityModel<Pedido> entityModel = EntityModel.of(pedido,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoHateoasController.class).findById(pedido.getIdPedido())).withSelfRel(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoHateoasController.class).findAll()).withRel("pedidos"),
            org.springframework.hateoas.Link.of(usuarioUrl + "/" + pedido.getIdCliente(), "cliente")
        );
        if (pedido.getDetallesPedido() != null) {
            pedido.getDetallesPedido().forEach(detalle -> {
                if (detalle.getIdProducto() != null) {
                    entityModel.add(
                        org.springframework.hateoas.Link.of(productoUrl + "/" + detalle.getIdProducto(), "producto-" + detalle.getIdProducto())
                    );
                }
                if (detalle.getIdSucursal() != null) {
                    entityModel.add(
                        org.springframework.hateoas.Link.of(sucursalUrl + "/" + detalle.getIdSucursal(), "sucursal-" + detalle.getIdSucursal())
                    );
                }
            });
        }
        return entityModel;
    }
}
