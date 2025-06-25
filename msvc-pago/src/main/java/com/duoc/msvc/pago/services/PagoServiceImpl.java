package com.duoc.msvc.pago.services;

import com.duoc.msvc.pago.clients.EnvioClient;
import com.duoc.msvc.pago.clients.PedidoClient;
import com.duoc.msvc.pago.dtos.PagoDTO;
import com.duoc.msvc.pago.dtos.pojos.PedidoClientDTO;
import com.duoc.msvc.pago.exceptions.PagoException;
import com.duoc.msvc.pago.models.Pago;
import com.duoc.msvc.pago.repositories.PagoRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.duoc.msvc.pago.dtos.PagoHateoasDTO;
import org.springframework.hateoas.CollectionModel;
import com.duoc.msvc.pago.assemblers.PagoDTOModelAssembler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PagoServiceImpl implements PagoService{
    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PedidoClient pedidoClient;

    @Autowired
    private EnvioClient envioClient;

    @Autowired
    private PagoDTOModelAssembler assembler;

    @Override
    public CollectionModel<PagoHateoasDTO> findAll() {
        return assembler.toCollectionModel(this.pagoRepository.findAll());
    }

    @Override
    public CollectionModel<PagoHateoasDTO> findByEstado(String estado) {
        return assembler.toCollectionModel(this.pagoRepository.findByEstado(estado));
    }

    @Override
    public PagoHateoasDTO findById(Long id) {
        Pago pago = this.pagoRepository.findById(id).orElseThrow(
                () -> new PagoException("El pago con id " + id + " no se encuentra en la base de datos")
        );
        return assembler.toModel(pago);
    }

    @Override
    public String updateEstadoById(Long idPedido, String nuevoEstado) {
        Pago pago = this.pagoRepository.findByIdPedido(idPedido);
        if (pago == null) {
            throw new PagoException("No existe un pago para el pedido con id " + idPedido);
        }

        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            throw new PagoException("El nuevo estado no puede estar vacío");
        }

        if ("Completado".equalsIgnoreCase(pago.getEstado())) {
            throw new PagoException("El pago ya está completado");
        }

        try {
            pago.setEstado(nuevoEstado);
            pagoRepository.save(pago);

            // Si el estado es "Completado", actualizamos el envío
            if ("Completado".equalsIgnoreCase(nuevoEstado)) {
                try {
                    String resultado = envioClient.updateEstadoById(idPedido, "Pendiente");
                    if (resultado == null || resultado.trim().isEmpty()) {
                        throw new PagoException("No se recibió respuesta del servicio de envíos");
                    }
                } catch (Exception e) {
                    // Si falla la actualización del envío, revertimos el estado del pago
                    pago.setEstado("Pendiente");
                    pagoRepository.save(pago);
                    throw new PagoException("Error al actualizar el estado del envío: " + e.getMessage());
                }
            }

            return pago.getEstado();
        } catch (Exception e) {
            throw new PagoException("Error al actualizar el estado: " + e.getMessage());
        }
    }

    @Override
    public PagoHateoasDTO save(Pago pago) {
        if (pago.getIdPedido() == null || pago.getIdPedido() == 0) {
            throw new PagoException("El idPedido no puede ser nulo ni igual a 0");
        }
        validarPedido(pago.getIdPedido());
        pago.setEstado("Pendiente");
        pago.setFecha(java.time.LocalDateTime.now());
        return assembler.toModel(this.pagoRepository.save(pago));
    }

    public void validarPedido(Long idPedido) {
        try {
            ResponseEntity<PedidoClientDTO> response = pedidoClient.findById(idPedido);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new PagoException("El pedido con ID " + idPedido + " no está disponible");
            }
        } catch (FeignException.NotFound ex) {
            throw new PagoException("El pedido con ID " + idPedido + " no existe");
        } catch (FeignException ex) {
            throw new PagoException("Error al validar el pedido: " + ex.getMessage());
        }
    }

    @Transactional
    @Override
    public PagoHateoasDTO updateById(Long id, Pago pago) {
        if (pago.getIdPedido() == null || pago.getIdPedido() == 0) {
            throw new PagoException("El idPedido no puede ser nulo ni igual a 0");
        }
        Pago pagoDb = pagoRepository.findById(id).orElseThrow(
            () -> new PagoException("El pago con id " + id + " no existe en la base de datos")
        );
        validarPedido(pago.getIdPedido());
        pagoDb.setMetodo(pago.getMetodo());
        pagoDb.setMonto(pago.getMonto());
        pagoDb.setEstado(pago.getEstado());
        return assembler.toModel(pagoRepository.save(pagoDb));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        pagoRepository.findById(id).orElseThrow(
            () -> new PagoException("El pago con id " + id + " no existe en la base de datos")
        );
        pagoRepository.deleteById(id);
    }
}
