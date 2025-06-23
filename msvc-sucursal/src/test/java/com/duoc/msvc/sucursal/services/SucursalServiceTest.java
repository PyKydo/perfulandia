package com.duoc.msvc.sucursal.services;

import com.duoc.msvc.sucursal.dtos.SucursalDTO;
import com.duoc.msvc.sucursal.exceptions.SucursalException;
import com.duoc.msvc.sucursal.models.entities.Sucursal;
import com.duoc.msvc.sucursal.repositories.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;
    @InjectMocks
    private SucursalServicelmpl sucursalService;

    private Sucursal sucursal;

    @BeforeEach
    void setUp() {
        sucursal = new Sucursal();
        sucursal.setIdSucursal(1L);
        sucursal.setDireccion("Av. Central 123");
        sucursal.setRegion("Metropolitana");
        sucursal.setComuna("Santiago");
        sucursal.setCantidadPersonal(10);
        sucursal.setHorariosAtencion("Lun-Vie 9:00-18:00");
    }

    @Test
    void shouldListAllSucursales() {
        when(sucursalRepository.findAll()).thenReturn(Arrays.asList(sucursal));
        List<SucursalDTO> result = sucursalService.findAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDireccion()).isEqualTo("Av. Central 123");
        verify(sucursalRepository).findAll();
    }

    @Test
    void shouldFindSucursalById() {
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        SucursalDTO dto = sucursalService.findById(1L);
        assertThat(dto.getDireccion()).isEqualTo("Av. Central 123");
        verify(sucursalRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenSucursalNotFound() {
        when(sucursalRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> sucursalService.findById(2L))
                .isInstanceOf(SucursalException.class)
                .hasMessageContaining("no existe");
        verify(sucursalRepository).findById(2L);
    }

    @Test
    void shouldCreateSucursal() {
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(sucursal);
        SucursalDTO dto = sucursalService.save(sucursal);
        assertThat(dto.getDireccion()).isEqualTo("Av. Central 123");
        verify(sucursalRepository).save(sucursal);
    }

    @Test
    void shouldUpdateSucursal() {
        Sucursal updated = new Sucursal();
        updated.setDireccion("Av. Nueva 456");
        updated.setRegion("Valparaíso");
        updated.setComuna("Viña del Mar");
        updated.setCantidadPersonal(15);
        updated.setHorariosAtencion("Lun-Sab 8:00-20:00");

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(updated);

        SucursalDTO dto = sucursalService.updateById(1L, updated);
        assertThat(dto.getDireccion()).isEqualTo("Av. Nueva 456");
        verify(sucursalRepository).findById(1L);
        verify(sucursalRepository).save(any(Sucursal.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdateSucursalNotFound() {
        when(sucursalRepository.findById(2L)).thenReturn(Optional.empty());
        Sucursal updated = new Sucursal();
        assertThatThrownBy(() -> sucursalService.updateById(2L, updated))
                .isInstanceOf(SucursalException.class)
                .hasMessageContaining("no encontrada");
        verify(sucursalRepository).findById(2L);
    }

    @Test
    void shouldDeleteSucursal() {
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        doNothing().when(sucursalRepository).deleteById(1L);
        sucursalService.deleteById(1L);
        verify(sucursalRepository).findById(1L);
        verify(sucursalRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeleteSucursalNotFound() {
        when(sucursalRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> sucursalService.deleteById(2L))
                .isInstanceOf(SucursalException.class)
                .hasMessageContaining("no encontrada");
        verify(sucursalRepository).findById(2L);
    }

    @Test
    void shouldConvertToDTO() {
        SucursalDTO dto = sucursalService.convertToDTO(sucursal);
        assertThat(dto.getDireccion()).isEqualTo("Av. Central 123");
        assertThat(dto.getRegion()).isEqualTo("Metropolitana");
        assertThat(dto.getCantidadPersonal()).isEqualTo(10);
    }
} 