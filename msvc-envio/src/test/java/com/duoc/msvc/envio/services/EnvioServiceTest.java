package com.duoc.msvc.envio.services;

import com.duoc.msvc.envio.clients.PedidoClient;
import com.duoc.msvc.envio.dtos.EnvioGetDTO;
import com.duoc.msvc.envio.exceptions.EnvioException;
import com.duoc.msvc.envio.models.entities.Envio;
import com.duoc.msvc.envio.repositories.EnvioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;
    @Mock
    private PedidoClient pedidoClient;
    @InjectMocks
    private EnvioServicelmpl envioService;

    private Envio envio;

    @BeforeEach
    void setUp() {
        envio = new Envio();
        envio.setIdEnvio(1L);
        envio.setIdPedido(100L);
        envio.setDireccion("Calle 1");
        envio.setRegion("Metropolitana");
        envio.setComuna("Santiago");
        envio.setCiudad("Santiago");
        envio.setCodigoPostal("1234567");
        envio.setCosto(BigDecimal.valueOf(5000));
        envio.setEstado("Pendiente");
    }

    @Test
    void shouldListAllEnvios() {
        when(envioRepository.findAll()).thenReturn(Arrays.asList(envio));
        List<EnvioGetDTO> result = envioService.findAll();
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDireccion()).isEqualTo("Calle 1");
        verify(envioRepository).findAll();
    }

    @Test
    void shouldFindEnvioById() {
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
        EnvioGetDTO result = envioService.findById(1L);
        assertThat(result).isNotNull();
        assertThat(result.getDireccion()).isEqualTo("Calle 1");
        verify(envioRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenEnvioNotFound() {
        when(envioRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> envioService.findById(2L))
                .isInstanceOf(EnvioException.class)
                .hasMessageContaining("no existe");
        verify(envioRepository).findById(2L);
    }

    @Test
    void shouldCreateEnvio() {
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);
        EnvioGetDTO dto = envioService.save(envio);
        assertThat(dto.getDireccion()).isEqualTo("Calle 1");
        assertThat(dto.getIdPedido()).isEqualTo(100L);
        verify(envioRepository).save(envio);
    }

    @Test
    void shouldCreateEnvioWithAutoCalculatedCost() {
        Envio envioSinCosto = new Envio();
        envioSinCosto.setIdPedido(100L);
        envioSinCosto.setDireccion("Calle 1");
        envioSinCosto.setRegion("Metropolitana");
        envioSinCosto.setComuna("Santiago");
        envioSinCosto.setCiudad("Santiago");
        envioSinCosto.setCodigoPostal("1234567");
        envioSinCosto.setCosto(null);

        when(envioRepository.save(any(Envio.class))).thenAnswer(invocation -> {
            Envio savedEnvio = invocation.getArgument(0);
            savedEnvio.setIdEnvio(1L);
            savedEnvio.setEstado("Pendiente");
            return savedEnvio;
        });

        EnvioGetDTO dto = envioService.save(envioSinCosto);
        
        assertThat(dto.getCosto()).isNotNull();
        assertThat(dto.getCosto()).isGreaterThan(BigDecimal.ZERO);
        assertThat(dto.getEstado()).isEqualTo("Pendiente");
        verify(envioRepository).save(any(Envio.class));
    }

    @Test
    void shouldUpdateEnvio() {
        Envio updated = new Envio();
        updated.setIdEnvio(1L);
        updated.setIdPedido(100L);
        updated.setDireccion("Calle 2");
        updated.setRegion("Valparaíso");
        updated.setComuna("Viña del Mar");
        updated.setCiudad("Viña del Mar");
        updated.setCodigoPostal("7654321");
        updated.setCosto(BigDecimal.valueOf(8000));
        updated.setEstado("Enviado");
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
        when(envioRepository.save(any(Envio.class))).thenReturn(updated);
        EnvioGetDTO dto = envioService.updateById(1L, updated);
        assertThat(dto.getDireccion()).isEqualTo("Calle 2");
        assertThat(dto.getEstado()).isEqualTo("Enviado");
        verify(envioRepository).findById(1L);
        verify(envioRepository).save(any(Envio.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdateEnvioNotFound() {
        when(envioRepository.findById(2L)).thenReturn(Optional.empty());
        Envio updated = new Envio();
        assertThatThrownBy(() -> envioService.updateById(2L, updated))
                .isInstanceOf(EnvioException.class)
                .hasMessageContaining("no existe");
        verify(envioRepository).findById(2L);
    }

    @Test
    void shouldDeleteEnvio() {
        when(envioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(envioRepository).deleteById(1L);
        envioService.deleteById(1L);
        verify(envioRepository).existsById(1L);
        verify(envioRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeleteEnvioNotFound() {
        when(envioRepository.existsById(2L)).thenReturn(false);
        assertThatThrownBy(() -> envioService.deleteById(2L))
                .isInstanceOf(EnvioException.class)
                .hasMessageContaining("no existe");
        verify(envioRepository).existsById(2L);
    }

    @Test
    void shouldConvertToDTO() {
        EnvioGetDTO dto = envioService.convertToDTO(envio);
        assertThat(dto.getDireccion()).isEqualTo("Calle 1");
        assertThat(dto.getRegion()).isEqualTo("Metropolitana");
        assertThat(dto.getCosto()).isEqualTo(BigDecimal.valueOf(5000));
    }

    @Test
    void shouldCalculateShippingCost() {
        BigDecimal costo = envioService.getCostoEnvio();
        
        assertThat(costo).isNotNull();
        assertThat(costo).isGreaterThan(BigDecimal.ZERO);
        assertThat(costo).isGreaterThanOrEqualTo(new BigDecimal("2000"));
    }

    @Test
    void shouldUpdateEstadoSuccessfully() {
        when(envioRepository.findByIdPedido(100L)).thenReturn(envio);
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);
        when(pedidoClient.updateEstadoById(100L, "Entregado")).thenReturn("Pedido actualizado");

        String result = envioService.updateEstadoById(100L, "Enviado");

        assertThat(result).isEqualTo("Enviado");
        verify(envioRepository).findByIdPedido(100L);
        verify(envioRepository).save(any(Envio.class));
        verify(pedidoClient).updateEstadoById(100L, "Entregado");
    }

    @Test
    void shouldUpdateEstadoAndNotifyPedidoWhenEnviado() {
        when(envioRepository.findByIdPedido(100L)).thenReturn(envio);
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);
        when(pedidoClient.updateEstadoById(100L, "Entregado")).thenReturn("Pedido actualizado");

        String result = envioService.updateEstadoById(100L, "Enviado");

        assertThat(result).isEqualTo("Enviado");
        verify(envioRepository).findByIdPedido(100L);
        verify(envioRepository).save(any(Envio.class));
        verify(pedidoClient).updateEstadoById(100L, "Entregado");
    }

    @Test
    void shouldThrowExceptionWhenUpdateEstadoWithNonExistentPedido() {
        when(envioRepository.findByIdPedido(999L)).thenReturn(null);

        assertThatThrownBy(() -> envioService.updateEstadoById(999L, "Enviado"))
                .isInstanceOf(EnvioException.class)
                .hasMessageContaining("No existe un envío para el pedido");

        verify(envioRepository).findByIdPedido(999L);
    }

    @Test
    void shouldThrowExceptionWhenUpdateEstadoWithNullEstado() {
        when(envioRepository.findByIdPedido(100L)).thenReturn(envio);
        assertThatThrownBy(() -> envioService.updateEstadoById(100L, null))
                .isInstanceOf(EnvioException.class)
                .hasMessageContaining("El nuevo estado no puede estar vacío");
        verify(envioRepository).findByIdPedido(100L);
    }

    @Test
    void shouldThrowExceptionWhenUpdateEstadoWithEmptyEstado() {
        when(envioRepository.findByIdPedido(100L)).thenReturn(envio);
        assertThatThrownBy(() -> envioService.updateEstadoById(100L, ""))
                .isInstanceOf(EnvioException.class)
                .hasMessageContaining("El nuevo estado no puede estar vacío");
        verify(envioRepository).findByIdPedido(100L);
    }

    @Test
    void shouldThrowExceptionWhenUpdateEstadoWithBlankEstado() {
        when(envioRepository.findByIdPedido(100L)).thenReturn(envio);
        assertThatThrownBy(() -> envioService.updateEstadoById(100L, "   "))
                .isInstanceOf(EnvioException.class)
                .hasMessageContaining("El nuevo estado no puede estar vacío");
        verify(envioRepository).findByIdPedido(100L);
    }

    @Test
    void shouldRevertEstadoWhenPedidoClientFails() {
        when(envioRepository.findByIdPedido(100L)).thenReturn(envio);
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);
        when(pedidoClient.updateEstadoById(100L, "Entregado")).thenThrow(new RuntimeException("Error de conexión"));

        assertThatThrownBy(() -> envioService.updateEstadoById(100L, "Enviado"))
                .isInstanceOf(EnvioException.class)
                .hasMessageContaining("Error al actualizar el estado del pedido");

        verify(envioRepository).findByIdPedido(100L);
        verify(envioRepository, times(3)).save(any(Envio.class));
        verify(pedidoClient).updateEstadoById(100L, "Entregado");
    }

    @Test
    void shouldRevertEstadoWhenPedidoClientReturnsEmptyResponse() {
        when(envioRepository.findByIdPedido(100L)).thenReturn(envio);
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);
        when(pedidoClient.updateEstadoById(100L, "Entregado")).thenReturn("");

        assertThatThrownBy(() -> envioService.updateEstadoById(100L, "Enviado"))
                .isInstanceOf(EnvioException.class)
                .hasMessageContaining("No se recibió respuesta del servicio de pedidos");

        verify(envioRepository).findByIdPedido(100L);
        verify(envioRepository, times(3)).save(any(Envio.class));
        verify(pedidoClient).updateEstadoById(100L, "Entregado");
    }

    @Test
    void shouldRevertEstadoWhenPedidoClientReturnsNullResponse() {
        when(envioRepository.findByIdPedido(100L)).thenReturn(envio);
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);
        when(pedidoClient.updateEstadoById(100L, "Entregado")).thenReturn(null);

        assertThatThrownBy(() -> envioService.updateEstadoById(100L, "Enviado"))
                .isInstanceOf(EnvioException.class)
                .hasMessageContaining("No se recibió respuesta del servicio de pedidos");

        verify(envioRepository).findByIdPedido(100L);
        verify(envioRepository, times(3)).save(any(Envio.class));
        verify(pedidoClient).updateEstadoById(100L, "Entregado");
    }

    @Test
    void shouldNotNotifyPedidoWhenEstadoIsNotEnviado() {
        when(envioRepository.findByIdPedido(100L)).thenReturn(envio);
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);

        String result = envioService.updateEstadoById(100L, "En Proceso");

        assertThat(result).isEqualTo("En Proceso");
        verify(envioRepository).findByIdPedido(100L);
        verify(envioRepository).save(any(Envio.class));
        verify(pedidoClient, never()).updateEstadoById(any(), any());
    }

    @Test
    void shouldHandleExceptionDuringEstadoUpdate() {
        when(envioRepository.findByIdPedido(100L)).thenReturn(envio);
        when(envioRepository.save(any(Envio.class))).thenThrow(new RuntimeException("Error de base de datos"));

        assertThatThrownBy(() -> envioService.updateEstadoById(100L, "Enviado"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error de base de datos");

        verify(envioRepository).findByIdPedido(100L);
        verify(envioRepository, times(2)).save(any(Envio.class));
    }

    @Test
    void shouldValidateCostoEnvioCalculation() {
        BigDecimal costo1 = envioService.getCostoEnvio();
        BigDecimal costo2 = envioService.getCostoEnvio();
        
        assertThat(costo1).isNotNull();
        assertThat(costo2).isNotNull();
        assertThat(costo1).isGreaterThanOrEqualTo(new BigDecimal("2000"));
        assertThat(costo2).isGreaterThanOrEqualTo(new BigDecimal("2000"));
    }

    @Test
    void shouldUpdateAllEnvioFields() {
        Envio updated = new Envio();
        updated.setDireccion("Nueva Dirección 456");
        updated.setRegion("Nueva Región");
        updated.setComuna("Nueva Comuna");
        updated.setCiudad("Nueva Ciudad");
        updated.setCodigoPostal("9876543");
        updated.setEstado("Entregado");
        updated.setFechaEstimadaEntrega(java.time.LocalDate.now().plusDays(3).atStartOfDay());

        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
        when(envioRepository.save(any(Envio.class))).thenReturn(updated);

        EnvioGetDTO dto = envioService.updateById(1L, updated);

        assertThat(dto.getDireccion()).isEqualTo("Nueva Dirección 456");
        assertThat(dto.getRegion()).isEqualTo("Nueva Región");
        assertThat(dto.getComuna()).isEqualTo("Nueva Comuna");
        assertThat(dto.getCiudad()).isEqualTo("Nueva Ciudad");
        assertThat(dto.getCodigoPostal()).isEqualTo("9876543");
        assertThat(dto.getEstado()).isEqualTo("Entregado");
        assertThat(dto.getFechaEstimadaEntrega()).isNotNull();

        verify(envioRepository).findById(1L);
        verify(envioRepository).save(any(Envio.class));
    }

    @Test
    void shouldValidateEnvioGetDTOConversion() {
        EnvioGetDTO dto = envioService.convertToDTO(envio);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getIdPedido()).isEqualTo(100L);
        assertThat(dto.getDireccion()).isEqualTo("Calle 1");
        assertThat(dto.getRegion()).isEqualTo("Metropolitana");
        assertThat(dto.getComuna()).isEqualTo("Santiago");
        assertThat(dto.getCiudad()).isEqualTo("Santiago");
        assertThat(dto.getCodigoPostal()).isEqualTo("1234567");
        assertThat(dto.getCosto()).isEqualTo(BigDecimal.valueOf(5000));
        assertThat(dto.getEstado()).isEqualTo("Pendiente");
    }
} 