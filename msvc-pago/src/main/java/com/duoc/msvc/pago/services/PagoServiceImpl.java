package com.duoc.msvc.pago.services;

import com.duoc.msvc.pago.clients.EnvioClient;
import com.duoc.msvc.pago.clients.PedidoClient;
import com.duoc.msvc.pago.dtos.pojos.PedidoClientDTO;
import com.duoc.msvc.pago.exceptions.PagoException;
import com.duoc.msvc.pago.models.Pago;
import com.duoc.msvc.pago.repositories.PagoRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import com.duoc.msvc.pago.controllers.PagoHateoasController;

import java.util.List;

@Service
public class PagoServiceImpl implements PagoService{
    
    private static final Logger logger = LoggerFactory.getLogger(PagoServiceImpl.class);
    
    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PedidoClient pedidoClient;

    @Autowired
    private EnvioClient envioClient;

    @Override
    public CollectionModel<EntityModel<Pago>> findAll() {
        logger.info("Iniciando búsqueda de todos los pagos");
        List<EntityModel<Pago>> pagos = this.pagoRepository.findAll().stream()
            .map(this::toEntityModel)
            .toList();
        logger.info("Búsqueda completada. Se encontraron {} pagos", pagos.size());
        return CollectionModel.of(pagos,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PagoHateoasController.class).findAll()).withSelfRel()
        );
    }

    @Override
    public CollectionModel<EntityModel<Pago>> findByEstado(String estado) {
        logger.info("Buscando pagos con estado: {}", estado);
        List<EntityModel<Pago>> pagos = this.pagoRepository.findByEstado(estado).stream()
            .map(this::toEntityModel)
            .toList();
        logger.info("Se encontraron {} pagos con estado: {}", pagos.size(), estado);
        return CollectionModel.of(pagos,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PagoHateoasController.class).findByEstado(estado)).withSelfRel()
        );
    }

    @Override
    public EntityModel<Pago> findById(Long id) {
        logger.info("Buscando pago con ID: {}", id);
        Pago pago = this.pagoRepository.findById(id).orElseThrow(
                () -> new PagoException("El pago con id " + id + " no se encuentra en la base de datos")
        );
        logger.info("Pago encontrado. Pedido ID: {}, Estado: {}, Monto: {}", pago.getIdPedido(), pago.getEstado(), pago.getMonto());
        return toEntityModel(pago);
    }

    @Override
    public String updateEstadoById(Long idPedido, String nuevoEstado) {
        logger.info("Iniciando actualización de estado de pago. Pedido ID: {}, Nuevo estado: {}", idPedido, nuevoEstado);
        
        Pago pago = this.pagoRepository.findByIdPedido(idPedido);
        if (pago == null) {
            logger.error("Error: No existe un pago para el pedido con id {}", idPedido);
            throw new PagoException("No existe un pago para el pedido con id " + idPedido);
        }

        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            logger.error("Error: El nuevo estado no puede estar vacío");
            throw new PagoException("El nuevo estado no puede estar vacío");
        }

        if ("Completado".equalsIgnoreCase(pago.getEstado())) {
            logger.error("Error: El pago ya está completado. Estado actual: {}", pago.getEstado());
            throw new PagoException("El pago ya está completado");
        }

        try {
            logger.info("Actualizando estado del pago de '{}' a '{}'", pago.getEstado(), nuevoEstado);
            pago.setEstado(nuevoEstado);
            pagoRepository.save(pago);

            if ("Completado".equalsIgnoreCase(nuevoEstado)) {
                logger.info("Actualizando estado del envío a 'Pendiente'");
                try {
                    String resultado = envioClient.updateEstadoById(idPedido, "Pendiente");
                    if (resultado == null || resultado.trim().isEmpty()) {
                        logger.error("Error: No se recibió respuesta del servicio de envíos");
                        throw new PagoException("No se recibió respuesta del servicio de envíos");
                    }
                    logger.info("Estado del envío actualizado exitosamente");
                } catch (Exception e) {
                    logger.error("Error al actualizar estado del envío: {}", e.getMessage(), e);
                    pago.setEstado("Pendiente");
                    pagoRepository.save(pago);
                    throw new PagoException("Error al actualizar el estado del envío: " + e.getMessage());
                }
            }

            logger.info("Estado del pago actualizado exitosamente a: {}", pago.getEstado());
            return pago.getEstado();
        } catch (Exception e) {
            logger.error("Error al actualizar el estado: {}", e.getMessage(), e);
            throw new PagoException("Error al actualizar el estado: " + e.getMessage());
        }
    }

    @Override
    public EntityModel<Pago> save(Pago pago) {
        logger.info("Iniciando creación de pago para pedido ID: {}", pago.getIdPedido());
        
        if (pago.getIdPedido() == null || pago.getIdPedido() == 0) {
            logger.error("Error: El idPedido no puede ser nulo ni igual a 0");
            throw new PagoException("El idPedido no puede ser nulo ni igual a 0");
        }
        
        logger.info("Validando existencia del pedido ID: {}", pago.getIdPedido());
        validarPedido(pago.getIdPedido());
        
        pago.setEstado("Pendiente");
        pago.setFecha(java.time.LocalDateTime.now());
        logger.info("Pago configurado - Estado: {}, Fecha: {}", pago.getEstado(), pago.getFecha());
        
        Pago pagoGuardado = this.pagoRepository.save(pago);
        logger.info("Pago creado exitosamente con ID: {}", pagoGuardado.getIdPago());
        return toEntityModel(pagoGuardado);
    }

    public void validarPedido(Long idPedido) {
        logger.info("Validando pedido con ID: {}", idPedido);
        try {
            ResponseEntity<PedidoClientDTO> response = pedidoClient.findById(idPedido);
            if (response.getStatusCode() != HttpStatus.OK) {
                logger.error("Error: El pedido con ID {} no está disponible. Status: {}", idPedido, response.getStatusCode());
                throw new PagoException("El pedido con ID " + idPedido + " no está disponible");
            }
            logger.info("Pedido validado exitosamente");
        } catch (FeignException.NotFound ex) {
            logger.error("Error: El pedido con ID {} no existe", idPedido);
            throw new PagoException("El pedido con ID " + idPedido + " no existe");
        } catch (FeignException ex) {
            logger.error("Error al validar el pedido: {}", ex.getMessage(), ex);
            throw new PagoException("Error al validar el pedido: " + ex.getMessage());
        }
    }

    @Transactional
    @Override
    public EntityModel<Pago> updateById(Long id, Pago pago) {
        logger.info("Iniciando actualización de pago con ID: {}", id);
        
        if (pago.getIdPedido() == null || pago.getIdPedido() == 0) {
            logger.error("Error: El idPedido no puede ser nulo ni igual a 0");
            throw new PagoException("El idPedido no puede ser nulo ni igual a 0");
        }
        
        Pago pagoDb = pagoRepository.findById(id).orElseThrow(
            () -> new PagoException("El pago con id " + id + " no existe en la base de datos")
        );
        
        logger.info("Validando pedido ID: {}", pago.getIdPedido());
        validarPedido(pago.getIdPedido());
        
        logger.info("Actualizando pago - Método: {} -> {}, Monto: {} -> {}, Estado: {} -> {}", 
                   pagoDb.getMetodo(), pago.getMetodo(), 
                   pagoDb.getMonto(), pago.getMonto(), 
                   pagoDb.getEstado(), pago.getEstado());
        
        pagoDb.setMetodo(pago.getMetodo());
        pagoDb.setMonto(pago.getMonto());
        pagoDb.setEstado(pago.getEstado());
        
        Pago pagoActualizado = pagoRepository.save(pagoDb);
        logger.info("Pago actualizado exitosamente");
        return toEntityModel(pagoActualizado);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        logger.info("Iniciando eliminación de pago con ID: {}", id);
        
        pagoRepository.findById(id).orElseThrow(
            () -> new PagoException("El pago con id " + id + " no existe en la base de datos")
        );
        
        pagoRepository.deleteById(id);
        logger.info("Pago eliminado exitosamente");
    }

    private EntityModel<Pago> toEntityModel(Pago pago) {
        return EntityModel.of(pago,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PagoHateoasController.class).findById(pago.getIdPago())).withSelfRel(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PagoHateoasController.class).findAll()).withRel("pagos"),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PagoHateoasController.class).findByEstado(pago.getEstado())).withRel("pagosPorEstado"),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PagoHateoasController.class).updateEstadoByIdPedido(pago.getIdPedido(), "nuevoEstado")).withRel("actualizarEstado")
        );
    }
}
