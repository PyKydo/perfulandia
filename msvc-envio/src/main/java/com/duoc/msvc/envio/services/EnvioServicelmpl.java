package com.duoc.msvc.envio.services;

import com.duoc.msvc.envio.clients.PedidoClient;
import com.duoc.msvc.envio.dtos.EnvioGetDTO;
import com.duoc.msvc.envio.exceptions.EnvioException;
import com.duoc.msvc.envio.models.entities.Envio;
import com.duoc.msvc.envio.repositories.EnvioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import com.duoc.msvc.envio.controllers.EnvioHateoasController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

@Service
public class EnvioServicelmpl implements EnvioService{

    private static final Logger logger = LoggerFactory.getLogger(EnvioServicelmpl.class);

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private PedidoClient pedidoClient;

    @Override
    public List<EnvioGetDTO> findAll() {
        logger.info("Iniciando búsqueda de todos los envíos");
        List<EnvioGetDTO> envios = this.envioRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
        logger.info("Búsqueda completada. Se encontraron {} envíos", envios.size());
        return envios;
    }

    @Override
    public EnvioGetDTO findById(Long id) {
        logger.info("Buscando envío con ID: {}", id);
        Envio envio = this.envioRepository.findById(id).orElseThrow(
                () -> new EnvioException("El envio con id "+ id + " no existe")
        );
        logger.info("Envío encontrado. Pedido ID: {}, Estado: {}", envio.getIdPedido(), envio.getEstado());
        return convertToDTO(envio);
    }

    @Override
    public EnvioGetDTO save(Envio envio) {
        logger.info("Iniciando creación de envío para pedido ID: {}", envio.getIdPedido());
        
        if(envio.getCosto() == null){
            envio.setCosto(getCostoEnvio());
            logger.info("Costo de envío calculado: {}", envio.getCosto());
        }

        envio.setEstado("Pendiente");

        Envio savedEnvio = this.envioRepository.save(envio);
        logger.info("Envío creado exitosamente con ID: {}", savedEnvio.getIdEnvio());
        return convertToDTO(savedEnvio);
    }

    @Override
    public EnvioGetDTO updateById(Long id, Envio envioActualizado) {
        logger.info("Iniciando actualización de envío con ID: {}", id);
        
        Envio envioExistente = envioRepository.findById(id)
                .orElseThrow(() -> new EnvioException("El envio con id " + id + " no existe"));

        logger.info("Actualizando datos del envío - Estado: {} -> {}", 
                   envioExistente.getEstado(), envioActualizado.getEstado());

        envioExistente.setDireccion(envioActualizado.getDireccion());
        envioExistente.setRegion(envioActualizado.getRegion());
        envioExistente.setCiudad(envioActualizado.getCiudad());
        envioExistente.setComuna(envioActualizado.getComuna());
        envioExistente.setCodigoPostal(envioActualizado.getCodigoPostal());
        envioExistente.setEstado(envioActualizado.getEstado());
        envioExistente.setFechaEstimadaEntrega(envioActualizado.getFechaEstimadaEntrega());

        Envio updatedEnvio = envioRepository.save(envioExistente);
        logger.info("Envío actualizado exitosamente");
        return convertToDTO(updatedEnvio);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Iniciando eliminación de envío con ID: {}", id);
        
        if (!envioRepository.existsById(id)) {
            throw new EnvioException("El envio con id " + id + " no existe");
        }
        
        envioRepository.deleteById(id);
        logger.info("Envío eliminado exitosamente");
    }

    @Transactional
    @Override
    public EnvioGetDTO convertToDTO(Envio envio) {
        EnvioGetDTO dto = new EnvioGetDTO();
        dto.setId(envio.getIdEnvio());
        dto.setCosto(envio.getCosto());
        dto.setCiudad(envio.getCiudad());
        dto.setDireccion(envio.getDireccion());
        dto.setComuna(envio.getComuna());
        dto.setEstado(envio.getEstado());
        dto.setRegion(envio.getRegion());
        dto.setCodigoPostal(envio.getCodigoPostal());
        dto.setIdPedido(envio.getIdPedido());
        dto.setFechaEstimadaEntrega(envio.getFechaEstimadaEntrega());
        return dto;
    }

    public BigDecimal getCostoEnvio() { // Simulación
        Random random = new Random();

        int distanciaKm = random.nextInt(96) + 5;
        double pesoRandom = 0.5 + (20.0 - 0.5) * random.nextDouble();
        BigDecimal pesoKg = BigDecimal.valueOf(pesoRandom).setScale(2, RoundingMode.HALF_UP);

        BigDecimal base = new BigDecimal("2000");
        BigDecimal tarifaPorKm = new BigDecimal("60");
        BigDecimal tarifaPorKg = new BigDecimal("300");

        BigDecimal costoDistancia = tarifaPorKm.multiply(BigDecimal.valueOf(distanciaKm));
        BigDecimal costoPeso = tarifaPorKg.multiply(pesoKg);
        BigDecimal costoTotal = base.add(costoDistancia).add(costoPeso);

        costoTotal = costoTotal.setScale(0, RoundingMode.HALF_UP);

        logger.debug("Cálculo de costo - Distancia: {}km, Peso: {}kg, Costo total: {}", 
                    distanciaKm, pesoKg, costoTotal);

        return costoTotal;
    }

    @Override
    public String updateEstadoById(Long idPedido, String nuevoEstado) {
        logger.info("Iniciando actualización de estado de envío. Pedido ID: {}, Nuevo estado: {}", idPedido, nuevoEstado);
        
        Envio envio = this.envioRepository.findByIdPedido(idPedido);
        if (envio == null) {
            logger.error("Error: No existe envío para el pedido con id {}", idPedido);
            throw new EnvioException("No existe un envío para el pedido con id " + idPedido);
        }

        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            logger.error("Error: El nuevo estado no puede estar vacío");
            throw new EnvioException("El nuevo estado no puede estar vacío");
        }

        String estadoActual = envio.getEstado();
        try {
            logger.info("Actualizando estado del envío de '{}' a '{}'", estadoActual, nuevoEstado);
            envio.setEstado(nuevoEstado);
            envioRepository.save(envio);

            if ("Enviado".equalsIgnoreCase(nuevoEstado)) {
                logger.info("Actualizando estado del pedido a 'Entregado'");
                try {
                    String resultado = pedidoClient.updateEstadoById(idPedido, "Entregado");
                    if (resultado == null || resultado.trim().isEmpty()) {
                        logger.error("Error: No se recibió respuesta del servicio de pedidos");
                        throw new EnvioException("No se recibió respuesta del servicio de pedidos");
                    }
                    logger.info("Estado del pedido actualizado exitosamente a 'Entregado'");
                } catch (Exception e) {
                    logger.error("Error al actualizar estado del pedido: {}", e.getMessage(), e);
                    envio.setEstado(estadoActual);
                    envioRepository.save(envio);
                    throw new EnvioException("Error al actualizar el estado del pedido: " + e.getMessage());
                }
            }

            logger.info("Estado del envío actualizado exitosamente a: {}", envio.getEstado());
            return envio.getEstado();
        } catch (Exception e) {
            logger.error("Error al actualizar el estado: {}", e.getMessage(), e);
            envio.setEstado(estadoActual);
            envioRepository.save(envio);
            throw new EnvioException("Error al actualizar el estado: " + e.getMessage());
        }
    }

    @Override
    public CollectionModel<EntityModel<Envio>> findAllHateoas() {
        List<Envio> envios = this.envioRepository.findAll();
        List<EntityModel<Envio>> entityModels = envios.stream()
            .map(this::toEntityModel)
            .toList();
        return CollectionModel.of(entityModels,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnvioHateoasController.class).findAll()).withSelfRel()
        );
    }

    @Override
    public EntityModel<Envio> findByIdHateoas(Long id) {
        Envio envio = this.envioRepository.findById(id).orElseThrow(
                () -> new EnvioException("El envio con id "+ id + " no existe")
        );
        return toEntityModel(envio);
    }

    private EntityModel<Envio> toEntityModel(Envio envio) {
        return EntityModel.of(envio,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnvioHateoasController.class).findById(envio.getIdEnvio())).withSelfRel(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnvioHateoasController.class).findAll()).withRel("envios"),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnvioHateoasController.class).getCostoEnvio()).withRel("calcularCosto"),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnvioHateoasController.class).updateEstadoByIdPedido(envio.getIdPedido(), "nuevoEstado")).withRel("actualizarEstado")
        );
    }
}
