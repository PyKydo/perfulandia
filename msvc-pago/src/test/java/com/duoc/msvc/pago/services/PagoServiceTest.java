package com.duoc.msvc.pago.services;

import com.duoc.msvc.pago.clients.PedidoClient;
import com.duoc.msvc.pago.clients.EnvioClient;
import com.duoc.msvc.pago.dtos.pojos.PedidoClientDTO;
import com.duoc.msvc.pago.exceptions.PagoException;
import com.duoc.msvc.pago.models.Pago;
import com.duoc.msvc.pago.repositories.PagoRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import feign.FeignException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;
    @Mock
    private PedidoClient pedidoClient;
    @Mock
    private EnvioClient envioClient;
    @InjectMocks
    private PagoServiceImpl pagoService;

    private Pago pago;

    @BeforeEach
    void setUp() throws Exception {
        pago = new Pago();
        pago.setIdPago(1L);
        pago.setIdPedido(100L);
        pago.setMetodo("Tarjeta");
        pago.setMonto(BigDecimal.valueOf(15000));
        pago.setEstado("Pendiente");
        pago.setFecha(LocalDateTime.of(2024, 1, 1, 10, 0, 0));
    }

    @Test
    void shouldListAllPagos() {
        when(pagoRepository.findAll()).thenReturn(Arrays.asList(pago));
        CollectionModel<EntityModel<Pago>> result = pagoService.findAll();
        assertThat(result.getContent()).hasSize(1);
        Pago pagoResult = result.getContent().iterator().next().getContent();
        assertThat(pagoResult.getMetodo()).isEqualTo("Tarjeta");
        verify(pagoRepository).findAll();
    }

    @Test
    void shouldFindPagoById() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        EntityModel<Pago> entityModel = pagoService.findById(1L);
        assertThat(entityModel.getContent().getMetodo()).isEqualTo("Tarjeta");
        verify(pagoRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenPagoNotFound() {
        when(pagoRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> pagoService.findById(2L))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("no se encuentra en la base de datos");
        verify(pagoRepository).findById(2L);
    }

    @Test
    void shouldCreatePago() {
        PedidoClientDTO mockPedido = new PedidoClientDTO();
        ResponseEntity<PedidoClientDTO> response = new ResponseEntity<>(mockPedido, HttpStatus.OK);
        when(pedidoClient.findById(100L)).thenReturn(response);
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        EntityModel<Pago> entityModel = pagoService.save(pago);
        assertThat(entityModel.getContent().getMetodo()).isEqualTo("Tarjeta");
        verify(pagoRepository).save(pago);
        verify(pedidoClient).findById(100L);
    }

    @Test
    void shouldUpdatePago() {
        PedidoClientDTO mockPedido = new PedidoClientDTO();
        ResponseEntity<PedidoClientDTO> response = new ResponseEntity<>(mockPedido, HttpStatus.OK);
        when(pedidoClient.findById(100L)).thenReturn(response);
        Pago updated = new Pago();
        updated.setMetodo("Efectivo");
        updated.setMonto(BigDecimal.valueOf(20000));
        updated.setEstado("Completado");
        updated.setFecha(LocalDateTime.now());
        updated.setIdPedido(100L);
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenReturn(updated);
        EntityModel<Pago> entityModel = pagoService.updateById(1L, updated);
        assertThat(entityModel.getContent().getMetodo()).isEqualTo("Efectivo");
        assertThat(entityModel.getContent().getEstado()).isEqualTo("Completado");
        verify(pagoRepository).findById(1L);
        verify(pagoRepository).save(any(Pago.class));
        verify(pedidoClient).findById(100L);
    }

    @Test
    void shouldThrowExceptionWhenUpdatePagoNotFound() {
        when(pagoRepository.findById(2L)).thenReturn(Optional.empty());
        Pago updated = new Pago();
        updated.setIdPedido(100L);
        assertThatThrownBy(() -> pagoService.updateById(2L, updated))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("no existe en la base de datos");
        verify(pagoRepository).findById(2L);
    }

    @Test
    void shouldDeletePago() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        doNothing().when(pagoRepository).deleteById(1L);
        pagoService.deleteById(1L);
        verify(pagoRepository).findById(1L);
        verify(pagoRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletePagoNotFound() {
        when(pagoRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> pagoService.deleteById(2L))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("no existe en la base de datos");
        verify(pagoRepository).findById(2L);
    }


    @Test
    void shouldFindPagosByEstado() {
        when(pagoRepository.findByEstado("Pendiente")).thenReturn(Arrays.asList(pago));
        CollectionModel<EntityModel<Pago>> result = pagoService.findByEstado("Pendiente");
        assertThat(result.getContent()).hasSize(1);
        Pago pagoResult = result.getContent().iterator().next().getContent();
        assertThat(pagoResult.getEstado()).isEqualTo("Pendiente");
        verify(pagoRepository).findByEstado("Pendiente");
    }

    @Test
    void shouldUpdateEstadoSuccessfully() {
        when(pagoRepository.findByIdPedido(100L)).thenReturn(pago);
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        when(envioClient.updateEstadoById(100L, "Pendiente")).thenReturn("Envío actualizado");

        String result = pagoService.updateEstadoById(100L, "Completado");

        assertThat(result).isEqualTo("Completado");
        verify(pagoRepository).findByIdPedido(100L);
        verify(pagoRepository).save(any(Pago.class));
        verify(envioClient).updateEstadoById(100L, "Pendiente");
    }

    @Test
    void shouldUpdateEstadoAndNotifyEnvioWhenCompletado() {
        when(pagoRepository.findByIdPedido(100L)).thenReturn(pago);
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        when(envioClient.updateEstadoById(100L, "Pendiente")).thenReturn("Envío actualizado");

        String result = pagoService.updateEstadoById(100L, "Completado");

        assertThat(result).isEqualTo("Completado");
        verify(pagoRepository).findByIdPedido(100L);
        verify(pagoRepository).save(any(Pago.class));
        verify(envioClient).updateEstadoById(100L, "Pendiente");
    }

    @Test
    void shouldThrowExceptionWhenUpdateEstadoWithNonExistentPedido() {
        when(pagoRepository.findByIdPedido(999L)).thenReturn(null);

        assertThatThrownBy(() -> pagoService.updateEstadoById(999L, "Completado"))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("No existe un pago para el pedido");

        verify(pagoRepository).findByIdPedido(999L);
    }

    @Test
    void shouldThrowExceptionWhenUpdateEstadoWithNullEstado() {
        when(pagoRepository.findByIdPedido(100L)).thenReturn(pago);
        assertThatThrownBy(() -> pagoService.updateEstadoById(100L, null))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("El nuevo estado no puede estar vacío");
        verify(pagoRepository).findByIdPedido(100L);
    }

    @Test
    void shouldThrowExceptionWhenUpdateEstadoWithEmptyEstado() {
        when(pagoRepository.findByIdPedido(100L)).thenReturn(pago);
        assertThatThrownBy(() -> pagoService.updateEstadoById(100L, ""))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("El nuevo estado no puede estar vacío");
        verify(pagoRepository).findByIdPedido(100L);
    }

    @Test
    void shouldThrowExceptionWhenUpdateEstadoWithBlankEstado() {
        when(pagoRepository.findByIdPedido(100L)).thenReturn(pago);
        assertThatThrownBy(() -> pagoService.updateEstadoById(100L, "   "))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("El nuevo estado no puede estar vacío");
        verify(pagoRepository).findByIdPedido(100L);
    }

    @Test
    void shouldThrowExceptionWhenUpdateEstadoAlreadyCompleted() {
        pago.setEstado("Completado");
        when(pagoRepository.findByIdPedido(100L)).thenReturn(pago);

        assertThatThrownBy(() -> pagoService.updateEstadoById(100L, "Completado"))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("El pago ya está completado");

        verify(pagoRepository).findByIdPedido(100L);
    }

    @Test
    void shouldRevertEstadoWhenEnvioClientFails() {
        when(pagoRepository.findByIdPedido(100L)).thenReturn(pago);
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        when(envioClient.updateEstadoById(100L, "Pendiente")).thenThrow(new RuntimeException("Error de conexión"));

        assertThatThrownBy(() -> pagoService.updateEstadoById(100L, "Completado"))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("Error al actualizar el estado del envío");

        verify(pagoRepository).findByIdPedido(100L);
        verify(pagoRepository, times(2)).save(any(Pago.class));
        verify(envioClient).updateEstadoById(100L, "Pendiente");
    }

    @Test
    void shouldRevertEstadoWhenEnvioClientReturnsEmptyResponse() {
        when(pagoRepository.findByIdPedido(100L)).thenReturn(pago);
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        when(envioClient.updateEstadoById(100L, "Pendiente")).thenReturn("");

        assertThatThrownBy(() -> pagoService.updateEstadoById(100L, "Completado"))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("No se recibió respuesta del servicio de envíos");

        verify(pagoRepository).findByIdPedido(100L);
        verify(pagoRepository, times(2)).save(any(Pago.class));
        verify(envioClient).updateEstadoById(100L, "Pendiente");
    }

    @Test
    void shouldRevertEstadoWhenEnvioClientReturnsNullResponse() {
        when(pagoRepository.findByIdPedido(100L)).thenReturn(pago);
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        when(envioClient.updateEstadoById(100L, "Pendiente")).thenReturn(null);

        assertThatThrownBy(() -> pagoService.updateEstadoById(100L, "Completado"))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("No se recibió respuesta del servicio de envíos");

        verify(pagoRepository).findByIdPedido(100L);
        verify(pagoRepository, times(2)).save(any(Pago.class));
        verify(envioClient).updateEstadoById(100L, "Pendiente");
    }

    @Test
    void shouldNotNotifyEnvioWhenEstadoIsNotCompletado() {
        when(pagoRepository.findByIdPedido(100L)).thenReturn(pago);
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        String result = pagoService.updateEstadoById(100L, "En Proceso");

        assertThat(result).isEqualTo("En Proceso");
        verify(pagoRepository).findByIdPedido(100L);
        verify(pagoRepository).save(any(Pago.class));
        verify(envioClient, never()).updateEstadoById(any(), any());
    }

    @Test
    void shouldHandleExceptionDuringEstadoUpdate() {
        when(pagoRepository.findByIdPedido(100L)).thenReturn(pago);
        when(pagoRepository.save(any(Pago.class))).thenThrow(new RuntimeException("Error de base de datos"));

        assertThatThrownBy(() -> pagoService.updateEstadoById(100L, "Completado"))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("Error al actualizar el estado");

        verify(pagoRepository).findByIdPedido(100L);
        verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatePagoWithNullIdPedido() {
        pago.setIdPedido(null);
        assertThatThrownBy(() -> pagoService.save(pago))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("no puede ser nulo ni igual a 0");
    }

    @Test
    void shouldThrowExceptionWhenCreatePagoWithZeroIdPedido() {
        pago.setIdPedido(0L);
        assertThatThrownBy(() -> pagoService.save(pago))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("no puede ser nulo ni igual a 0");
    }

    @Test
    void shouldThrowExceptionWhenCreatePagoWithNonExistentPedido() {
        ResponseEntity<PedidoClientDTO> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(pedidoClient.findById(999L)).thenReturn(response);

        pago.setIdPedido(999L);
        assertThatThrownBy(() -> pagoService.save(pago))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("no está disponible");

        verify(pedidoClient).findById(999L);
    }

    @Test
    void shouldThrowExceptionWhenCreatePagoWithPedidoNotFound() {
        when(pedidoClient.findById(999L)).thenThrow(FeignException.NotFound.class);

        pago.setIdPedido(999L);
        assertThatThrownBy(() -> pagoService.save(pago))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("no existe");

        verify(pedidoClient).findById(999L);
    }

    @Test
    void shouldThrowExceptionWhenCreatePagoWithPedidoClientError() {
        when(pedidoClient.findById(999L)).thenThrow(new RuntimeException("Error de conexión"));

        pago.setIdPedido(999L);
        assertThatThrownBy(() -> pagoService.save(pago))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error de conexión");

        verify(pedidoClient).findById(999L);
    }

    @Test
    void shouldSetDefaultValuesWhenCreatePago() {
        PedidoClientDTO mockPedido = new PedidoClientDTO();
        ResponseEntity<PedidoClientDTO> response = new ResponseEntity<>(mockPedido, HttpStatus.OK);
        when(pedidoClient.findById(100L)).thenReturn(response);
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> {
            Pago savedPago = invocation.getArgument(0);
            savedPago.setIdPago(1L);
            savedPago.setEstado("Pendiente");
            savedPago.setFecha(LocalDateTime.now());
            return savedPago;
        });

        pago.setEstado(null);
        pago.setFecha(null);
        EntityModel<Pago> entityModel = pagoService.save(pago);

        assertThat(entityModel.getContent().getEstado()).isEqualTo("Pendiente");
        assertThat(entityModel.getContent().getFecha()).isNotNull();
        verify(pagoRepository).save(any(Pago.class));
        verify(pedidoClient).findById(100L);
    }

    @Test
    void shouldThrowExceptionWhenUpdatePagoWithNullIdPedido() {
        Pago updated = new Pago();
        updated.setIdPedido(null);

        assertThatThrownBy(() -> pagoService.updateById(1L, updated))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("no puede ser nulo ni igual a 0");
    }

    @Test
    void shouldThrowExceptionWhenUpdatePagoWithZeroIdPedido() {
        Pago updated = new Pago();
        updated.setIdPedido(0L);

        assertThatThrownBy(() -> pagoService.updateById(1L, updated))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("no puede ser nulo ni igual a 0");
    }

    @Test
    void shouldUpdateAllPagoFields() {
        PedidoClientDTO mockPedido = new PedidoClientDTO();
        ResponseEntity<PedidoClientDTO> response = new ResponseEntity<>(mockPedido, HttpStatus.OK);
        when(pedidoClient.findById(100L)).thenReturn(response);

        Pago updated = new Pago();
        updated.setMetodo("Transferencia");
        updated.setMonto(BigDecimal.valueOf(25000));
        updated.setEstado("Completado");
        updated.setIdPedido(100L);

        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenReturn(updated);

        EntityModel<Pago> entityModel = pagoService.updateById(1L, updated);

        assertThat(entityModel.getContent().getMetodo()).isEqualTo("Transferencia");
        assertThat(entityModel.getContent().getMonto()).isEqualTo(BigDecimal.valueOf(25000));
        assertThat(entityModel.getContent().getEstado()).isEqualTo("Completado");
        assertThat(entityModel.getContent().getIdPedido()).isEqualTo(100L);

        verify(pagoRepository).findById(1L);
        verify(pagoRepository).save(any(Pago.class));
        verify(pedidoClient).findById(100L);
    }

    @Test
    void shouldValidatePagoEntityModelStructure() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        EntityModel<Pago> entityModel = pagoService.findById(1L);

        assertThat(entityModel).isNotNull();
        assertThat(entityModel.getContent()).isNotNull();
        assertThat(entityModel.getContent().getIdPago()).isEqualTo(1L);
        assertThat(entityModel.getContent().getIdPedido()).isEqualTo(100L);
        assertThat(entityModel.getContent().getMetodo()).isEqualTo("Tarjeta");
        assertThat(entityModel.getContent().getMonto()).isEqualTo(BigDecimal.valueOf(15000));
        assertThat(entityModel.getContent().getEstado()).isEqualTo("Pendiente");
        assertThat(entityModel.getContent().getFecha()).isNotNull();

        verify(pagoRepository).findById(1L);
    }
} 