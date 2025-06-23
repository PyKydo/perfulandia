package com.duoc.msvc.pago.services;

import com.duoc.msvc.pago.dtos.PagoDTO;
import com.duoc.msvc.pago.exceptions.PagoException;
import com.duoc.msvc.pago.models.Pago;
import com.duoc.msvc.pago.repositories.PagoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;
    @InjectMocks
    private PagoServiceImpl pagoService;

    private Pago pago;

    @BeforeEach
    void setUp() {
        pago = new Pago();
        pago.setIdPago(1L);
        pago.setIdPedido(100L);
        pago.setMetodo("Tarjeta");
        pago.setMonto(BigDecimal.valueOf(15000));
        pago.setEstado("Pendiente");
        pago.setFecha("01/01/2024 10:00:00");
    }

    @Test
    void shouldListAllPagos() {
        when(pagoRepository.findAll()).thenReturn(Arrays.asList(pago));
        List<PagoDTO> result = pagoService.findAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMetodo()).isEqualTo("Tarjeta");
        verify(pagoRepository).findAll();
    }

    @Test
    void shouldFindPagoById() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        PagoDTO dto = pagoService.findById(1L);
        assertThat(dto.getMetodo()).isEqualTo("Tarjeta");
        verify(pagoRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenPagoNotFound() {
        when(pagoRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> pagoService.findById(2L))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("no se encuentra");
        verify(pagoRepository).findById(2L);
    }

    @Test
    void shouldCreatePago() {
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        PagoDTO dto = pagoService.save(pago);
        assertThat(dto.getMetodo()).isEqualTo("Tarjeta");
        verify(pagoRepository).save(pago);
    }

    @Test
    void shouldUpdatePago() {
        Pago updated = new Pago();
        updated.setMetodo("Efectivo");
        updated.setMonto(BigDecimal.valueOf(20000));
        updated.setEstado("Completado");
        updated.setFecha("02/01/2024 12:00:00");
        updated.setIdPedido(100L);

        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenReturn(updated);

        PagoDTO dto = pagoService.updateById(1L, updated);
        assertThat(dto.getMetodo()).isEqualTo("Efectivo");
        assertThat(dto.getEstado()).isEqualTo("Completado");
        verify(pagoRepository).findById(1L);
        verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatePagoNotFound() {
        when(pagoRepository.findById(2L)).thenReturn(Optional.empty());
        Pago updated = new Pago();
        assertThatThrownBy(() -> pagoService.updateById(2L, updated))
                .isInstanceOf(PagoException.class)
                .hasMessageContaining("no se encuentra");
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
                .hasMessageContaining("no se encuentra");
        verify(pagoRepository).findById(2L);
    }

    @Test
    void shouldConvertToDTO() {
        PagoDTO dto = pagoService.convertToDTO(pago);
        assertThat(dto.getMetodo()).isEqualTo("Tarjeta");
        assertThat(dto.getMonto()).isEqualTo(BigDecimal.valueOf(15000));
        assertThat(dto.getEstado()).isEqualTo("Pendiente");
    }
} 