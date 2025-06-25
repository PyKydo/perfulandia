package com.duoc.msvc.envio.services;

import com.duoc.msvc.envio.dtos.EnvioDTO;
import com.duoc.msvc.envio.dtos.EnvioGetDTO;
import com.duoc.msvc.envio.dtos.EnvioHateoasDTO;
import com.duoc.msvc.envio.exceptions.EnvioException;
import com.duoc.msvc.envio.models.entities.Envio;
import com.duoc.msvc.envio.repositories.EnvioRepository;
import com.duoc.msvc.envio.assemblers.EnvioDTOModelAssembler;
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
    private EnvioDTOModelAssembler assembler;
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
} 