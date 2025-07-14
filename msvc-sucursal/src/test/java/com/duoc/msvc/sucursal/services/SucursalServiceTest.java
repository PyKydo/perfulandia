package com.duoc.msvc.sucursal.services;

import com.duoc.msvc.sucursal.clients.ProductoClient;
import com.duoc.msvc.sucursal.dtos.SucursalCreateDTO;
import com.duoc.msvc.sucursal.dtos.SucursalGetDTO;
import com.duoc.msvc.sucursal.dtos.SucursalUpdateDTO;
import com.duoc.msvc.sucursal.dtos.pojos.ProductoDTO;
import com.duoc.msvc.sucursal.exceptions.SucursalException;
import com.duoc.msvc.sucursal.models.entities.Inventario;
import com.duoc.msvc.sucursal.models.entities.Sucursal;
import com.duoc.msvc.sucursal.repositories.SucursalRepository;
import com.duoc.msvc.sucursal.services.SucursalServicelmpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public
class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;
    
    @Mock
    private ProductoClient productoClient;
    
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
        List<SucursalGetDTO> result = sucursalService.findAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDireccion()).isEqualTo("Av. Central 123");
        verify(sucursalRepository).findAll();
    }

    @Test
    void shouldFindSucursalById() {
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        SucursalGetDTO dto = sucursalService.findById(1L);
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
        SucursalCreateDTO createDTO = new SucursalCreateDTO();
        createDTO.setDireccion(sucursal.getDireccion());
        createDTO.setRegion(sucursal.getRegion());
        createDTO.setComuna(sucursal.getComuna());
        createDTO.setCantidadPersonal(sucursal.getCantidadPersonal());
        createDTO.setHorariosAtencion(sucursal.getHorariosAtencion());
        SucursalGetDTO dto = sucursalService.save(createDTO);
        assertThat(dto.getDireccion()).isEqualTo("Av. Central 123");
        verify(sucursalRepository).save(any(Sucursal.class));
    }

    @Test
    void shouldUpdateSucursal() {
        SucursalUpdateDTO updated = new SucursalUpdateDTO();
        updated.setDireccion("Av. Nueva 456");
        updated.setRegion("Valparaíso");
        updated.setComuna("Viña del Mar");
        updated.setCantidadPersonal(15);
        updated.setHorariosAtencion("Lun-Sab 8:00-20:00");

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(sucursal);

        SucursalGetDTO dto = sucursalService.updateById(1L, updated);
        assertThat(dto.getDireccion()).isEqualTo("Av. Nueva 456");
        verify(sucursalRepository).findById(1L);
        verify(sucursalRepository).save(any(Sucursal.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdateSucursalNotFound() {
        when(sucursalRepository.findById(2L)).thenReturn(Optional.empty());
        SucursalUpdateDTO updated = new SucursalUpdateDTO();
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
        SucursalGetDTO dto = sucursalService.convertToDTO(sucursal);
        assertThat(dto.getDireccion()).isEqualTo("Av. Central 123");
        assertThat(dto.getRegion()).isEqualTo("Metropolitana");
        assertThat(dto.getCantidadPersonal()).isEqualTo(10);
    }

    @Test
    void shouldFindByBestStock() {
        when(sucursalRepository.countInventariosByProducto(1L)).thenReturn(1L);
        when(sucursalRepository.countInventariosWithStockByProducto(1L)).thenReturn(1L);
        when(sucursalRepository.findBySucursalBestStock(1L)).thenReturn(sucursal);

        SucursalGetDTO result = sucursalService.findByBestStock(1L);
        assertThat(result).isNotNull();
        assertThat(result.getDireccion()).isEqualTo("Av. Central 123");
        verify(sucursalRepository).countInventariosByProducto(1L);
        verify(sucursalRepository).countInventariosWithStockByProducto(1L);
        verify(sucursalRepository).findBySucursalBestStock(1L);
    }

    @Test
    void shouldThrowExceptionWhenProductoNotInInventario() {
        when(sucursalRepository.countInventariosByProducto(999L)).thenReturn(0L);

        assertThatThrownBy(() -> sucursalService.findByBestStock(999L))
                .isInstanceOf(SucursalException.class)
                .hasMessageContaining("no existe en ningún inventario");

        verify(sucursalRepository).countInventariosByProducto(999L);
        verify(sucursalRepository, never()).findBySucursalBestStock(any());
    }

    @Test
    void shouldThrowExceptionWhenProductoExistsButNoStock() {
        when(sucursalRepository.countInventariosByProducto(1L)).thenReturn(1L);
        when(sucursalRepository.countInventariosWithStockByProducto(1L)).thenReturn(0L);

        assertThatThrownBy(() -> sucursalService.findByBestStock(1L))
                .isInstanceOf(SucursalException.class)
                .hasMessageContaining("existe en inventarios pero no tiene stock disponible");

        verify(sucursalRepository).countInventariosByProducto(1L);
        verify(sucursalRepository).countInventariosWithStockByProducto(1L);
        verify(sucursalRepository, never()).findBySucursalBestStock(any());
    }

    @Test
    void shouldUpdateStockSuccessfully() {
        Inventario inventario = new Inventario();
        inventario.setIdInventario(1L);
        inventario.setIdProducto(100L);
        inventario.setStock(50);

        sucursal.setInventarios(Arrays.asList(inventario));

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(sucursal);

        sucursalService.updateStock(1L, 1L, 75);

        verify(sucursalRepository).findById(1L);
        verify(sucursalRepository).save(any(Sucursal.class));
        assertThat(inventario.getStock()).isEqualTo(75);
    }

    @Test
    void shouldThrowExceptionWhenUpdateStockSucursalNotFound() {
        when(sucursalRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sucursalService.updateStock(999L, 1L, 75))
                .isInstanceOf(SucursalException.class)
                .hasMessageContaining("Sucursal no encontrada");

        verify(sucursalRepository).findById(999L);
    }

    @Test
    void shouldThrowExceptionWhenUpdateStockInventarioNotFound() {
        Inventario inventario = new Inventario();
        inventario.setIdInventario(1L);
        inventario.setIdProducto(100L);
        inventario.setStock(50);

        sucursal.setInventarios(Arrays.asList(inventario));

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));

        assertThatThrownBy(() -> sucursalService.updateStock(1L, 999L, 75))
                .isInstanceOf(SucursalException.class)
                .hasMessageContaining("Inventario no encontrado");

        verify(sucursalRepository).findById(1L);
    }

    @Test
    void shouldUpdateStockToZero() {
        Inventario inventario = new Inventario();
        inventario.setIdInventario(1L);
        inventario.setIdProducto(100L);
        inventario.setStock(50);

        sucursal.setInventarios(Arrays.asList(inventario));

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(sucursal);

        sucursalService.updateStock(1L, 1L, 0);

        verify(sucursalRepository).findById(1L);
        verify(sucursalRepository).save(any(Sucursal.class));
        assertThat(inventario.getStock()).isEqualTo(0);
    }

    @Test
    void shouldConvertToDTOWithProductoInfo() {
        Inventario inventario = new Inventario();
        inventario.setIdInventario(1L);
        inventario.setIdProducto(100L);
        inventario.setStock(50);

        sucursal.setInventarios(Arrays.asList(inventario));

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(100L);
        productoDTO.setNombre("Perfume Test");
        productoDTO.setMarca("Marca Test");
        
        try {
            when(productoClient.findById(100L)).thenReturn(productoDTO);
        } catch (Exception e) {
        }

        SucursalGetDTO dto = sucursalService.findById(1L);

        assertThat(dto.getInventarios()).hasSize(1);
        assertThat(dto.getInventarios().get(0).getIdProducto()).isEqualTo(100L);
        assertThat(dto.getInventarios().get(0).getStock()).isEqualTo(50);
        assertThat(dto.getInventarios().get(0).getNombreProducto()).isEqualTo("Perfume Test");
        assertThat(dto.getInventarios().get(0).getMarcaProducto()).isEqualTo("Marca Test");

        verify(sucursalRepository).findById(1L);
        verify(productoClient).findById(100L);
    }

    @Test
    void shouldHandleProductoClientException() {
        Inventario inventario = new Inventario();
        inventario.setIdInventario(1L);
        inventario.setIdProducto(100L);
        inventario.setStock(50);

        sucursal.setInventarios(Arrays.asList(inventario));

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        try {
            when(productoClient.findById(100L)).thenThrow(new RuntimeException("Error de conexión"));
        } catch (Exception e) {
        }

        SucursalGetDTO dto = sucursalService.findById(1L);

        assertThat(dto.getInventarios()).hasSize(1);
        assertThat(dto.getInventarios().get(0).getNombreProducto()).isEqualTo("Desconocido");
        assertThat(dto.getInventarios().get(0).getMarcaProducto()).isEqualTo("Desconocida");

        verify(sucursalRepository).findById(1L);
    }

    @Test
    void shouldCreateSucursalWithNullValues() {
        SucursalCreateDTO createDTO = new SucursalCreateDTO();
        createDTO.setDireccion(null);
        createDTO.setRegion(null);
        createDTO.setComuna(null);
        createDTO.setCantidadPersonal(null);
        createDTO.setHorariosAtencion(null);

        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(sucursal);

        SucursalGetDTO result = sucursalService.save(createDTO);
        assertThat(result).isNotNull();
        verify(sucursalRepository).save(any(Sucursal.class));
    }

    @Test
    void shouldUpdateSucursalWithPartialData() {
        SucursalUpdateDTO partialUpdate = new SucursalUpdateDTO();
        partialUpdate.setDireccion("Nueva Dirección");
        partialUpdate.setCantidadPersonal(20);

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(sucursal);

        SucursalGetDTO result = sucursalService.updateById(1L, partialUpdate);
        assertThat(result.getDireccion()).isEqualTo("Nueva Dirección");
        verify(sucursalRepository).findById(1L);
        verify(sucursalRepository).save(any(Sucursal.class));
    }

    @Test
    void shouldHandleEmptyInventarios() {
        sucursal.setInventarios(new ArrayList<>());

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));

        SucursalGetDTO dto = sucursalService.findById(1L);
        assertThat(dto.getInventarios()).isEmpty();
        verify(sucursalRepository).findById(1L);
    }

    @Test
    void shouldHandleMultipleInventarios() {
        Inventario inventario1 = new Inventario();
        inventario1.setIdInventario(1L);
        inventario1.setIdProducto(100L);
        inventario1.setStock(50);

        Inventario inventario2 = new Inventario();
        inventario2.setIdInventario(2L);
        inventario2.setIdProducto(200L);
        inventario2.setStock(30);

        sucursal.setInventarios(Arrays.asList(inventario1, inventario2));

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        ProductoDTO producto1 = new ProductoDTO();
        producto1.setId(100L);
        producto1.setNombre("Perfume 1");
        producto1.setMarca("Marca 1");
        
        ProductoDTO producto2 = new ProductoDTO();
        producto2.setId(200L);
        producto2.setNombre("Perfume 2");
        producto2.setMarca("Marca 2");
        
        try {
            when(productoClient.findById(100L)).thenReturn(producto1);
            when(productoClient.findById(200L)).thenReturn(producto2);
        } catch (Exception e) {
        }

        SucursalGetDTO dto = sucursalService.findById(1L);
        assertThat(dto.getInventarios()).hasSize(2);
        assertThat(dto.getInventarios().get(0).getNombreProducto()).isEqualTo("Perfume 1");
        assertThat(dto.getInventarios().get(1).getNombreProducto()).isEqualTo("Perfume 2");
        verify(sucursalRepository).findById(1L);
        verify(productoClient).findById(100L);
        verify(productoClient).findById(200L);
    }

    @Test
    void shouldThrowExceptionWhenConvertToDTOWithNullSucursal() {
        assertThatThrownBy(() -> sucursalService.convertToDTO(null))
                .isInstanceOf(SucursalException.class)
                .hasMessageContaining("no puede ser null");
    }

    @Test
    void shouldFindAllHateoas() {
        when(sucursalRepository.findAll()).thenReturn(Arrays.asList(sucursal));
        
        var result = sucursalService.findAllHateoas();
        
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getLinks()).isNotEmpty();
        verify(sucursalRepository).findAll();
    }

    @Test
    void shouldFindByIdHateoas() {
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        
        var result = sucursalService.findByIdHateoas(1L);
        
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isNotNull();
        assertThat(result.getContent().getIdSucursal()).isEqualTo(1L);
        assertThat(result.getLinks()).isNotEmpty();
        verify(sucursalRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenFindByIdHateoasNotFound() {
        when(sucursalRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> sucursalService.findByIdHateoas(999L))
                .isInstanceOf(SucursalException.class)
                .hasMessageContaining("no existe");
        
        verify(sucursalRepository).findById(999L);
    }

    @Test
    void shouldGetProductoDisponibilidadWithStock() {
        when(sucursalRepository.countInventariosByProducto(1L)).thenReturn(1L);
        when(sucursalRepository.countInventariosWithStockByProducto(1L)).thenReturn(1L);
        
        String result = sucursalService.getProductoDisponibilidad(1L);
        
        assertThat(result).contains("tiene stock disponible");
        assertThat(result).contains("1");
        verify(sucursalRepository).countInventariosByProducto(1L);
        verify(sucursalRepository).countInventariosWithStockByProducto(1L);
    }

    @Test
    void shouldGetProductoDisponibilidadWithoutStock() {
        when(sucursalRepository.countInventariosByProducto(1L)).thenReturn(1L);
        when(sucursalRepository.countInventariosWithStockByProducto(1L)).thenReturn(0L);
        
        String result = sucursalService.getProductoDisponibilidad(1L);
        
        assertThat(result).contains("no tiene stock disponible");
        verify(sucursalRepository).countInventariosByProducto(1L);
        verify(sucursalRepository).countInventariosWithStockByProducto(1L);
    }

    @Test
    void shouldGetProductoDisponibilidadNotInInventario() {
        when(sucursalRepository.countInventariosByProducto(999L)).thenReturn(0L);
        
        String result = sucursalService.getProductoDisponibilidad(999L);
        
        assertThat(result).contains("no está registrado");
        verify(sucursalRepository).countInventariosByProducto(999L);
        verify(sucursalRepository, never()).countInventariosWithStockByProducto(any());
    }

    @Test
    void shouldHandleNullInventariosInConvertToDTO() {
        sucursal.setInventarios(null);
        
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        
        SucursalGetDTO dto = sucursalService.findById(1L);
        
        assertThat(dto.getInventarios()).isEmpty();
        verify(sucursalRepository).findById(1L);
    }

    @Test
    void shouldCreateSucursalWithInventarios() {
        Sucursal nuevaSucursal = new Sucursal();
        nuevaSucursal.setIdSucursal(1L);
        nuevaSucursal.setDireccion("Nueva Dirección");
        nuevaSucursal.setRegion("Nueva Región");
        nuevaSucursal.setComuna("Nueva Comuna");
        nuevaSucursal.setCantidadPersonal(5);
        nuevaSucursal.setHorariosAtencion("Lun-Vie 8:00-17:00");
        
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(nuevaSucursal);
        
        SucursalCreateDTO createDTO = new SucursalCreateDTO();
        createDTO.setDireccion("Nueva Dirección");
        createDTO.setRegion("Nueva Región");
        createDTO.setComuna("Nueva Comuna");
        createDTO.setCantidadPersonal(5);
        createDTO.setHorariosAtencion("Lun-Vie 8:00-17:00");
        
        SucursalGetDTO result = sucursalService.save(createDTO);
        
        assertThat(result).isNotNull();
        assertThat(result.getDireccion()).isEqualTo("Nueva Dirección");
        verify(sucursalRepository).save(any(Sucursal.class));
    }


}
