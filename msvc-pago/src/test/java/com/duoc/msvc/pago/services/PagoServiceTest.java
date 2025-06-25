package com.duoc.msvc.pago.services;

import com.duoc.msvc.pago.clients.PedidoClient;
import com.duoc.msvc.pago.clients.EnvioClient;
import com.duoc.msvc.pago.dtos.PagoDTO;
import com.duoc.msvc.pago.dtos.pojos.PedidoClientDTO;
import com.duoc.msvc.pago.exceptions.PagoException;
import com.duoc.msvc.pago.models.Pago;
import com.duoc.msvc.pago.repositories.PagoRepository;
import com.duoc.msvc.pago.dtos.PagoHateoasDTO;
import org.springframework.hateoas.CollectionModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.duoc.msvc.pago.assemblers.PagoDTOModelAssembler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
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
        pago.setFecha(LocalDateTime.of(2024, 1, 1, 10, 0, 0));       PagoDTOModelAssembler assembler = new PagoDTOModelAssembler();
        java.lang.reflect.Field assemblerField = PagoServiceImpl.class.getDeclaredField("assembler");
        assemblerField.setAccessible(true);
        assemblerField.set(pagoService, assembler);
    }

    @Test
    void shouldListAllPagos() {
        when(pagoRepository.findAll()).thenReturn(Arrays.asList(pago));
        CollectionModel<PagoHateoasDTO> result = pagoService.findAll();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().iterator().next().getMetodoPago()).isEqualTo("Tarjeta");
        verify(pagoRepository).findAll();
    }

    @Test
    void shouldFindPagoById() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        PagoHateoasDTO dto = pagoService.findById(1L);
        assertThat(dto.getMetodoPago()).isEqualTo("Tarjeta");
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
        PagoHateoasDTO dto = pagoService.save(pago);
        assertThat(dto.getMetodoPago()).isEqualTo("Tarjeta");
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
        PagoHateoasDTO dto = pagoService.updateById(1L, updated);
        assertThat(dto.getMetodoPago()).isEqualTo("Efectivo");
        assertThat(dto.getEstado()).isEqualTo("Completado");
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
} 