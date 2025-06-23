package com.duoc.msvc.producto.services;

import com.duoc.msvc.producto.dtos.ProductoDTO;
import com.duoc.msvc.producto.exceptions.ProductoException;
import com.duoc.msvc.producto.models.entities.Producto;
import com.duoc.msvc.producto.repositories.ProductoRepository;
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
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;
    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setIdProducto(1L);
        producto.setNombre("Aspirina");
        producto.setMarca("Bayer");
        producto.setPrecio(BigDecimal.valueOf(1990));
        producto.setDescripcion("Analgésico");
        producto.setImagenRepresentativaURL("http://img.com/aspirina.jpg");
        producto.setCategoria("Medicamento");
        producto.setPorcentajeConcentracion(0.5);
    }

    @Test
    void shouldListAllProductos() {
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto));
        List<ProductoDTO> result = productoService.findAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Aspirina");
        verify(productoRepository).findAll();
    }

    @Test
    void shouldFindProductoById() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        ProductoDTO dto = productoService.findById(1L);
        assertThat(dto.getNombre()).isEqualTo("Aspirina");
        verify(productoRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenProductoNotFound() {
        when(productoRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> productoService.findById(2L))
                .isInstanceOf(ProductoException.class)
                .hasMessageContaining("no existe");
        verify(productoRepository).findById(2L);
    }

    @Test
    void shouldCreateProducto() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        ProductoDTO dto = productoService.save(producto);
        assertThat(dto.getNombre()).isEqualTo("Aspirina");
        verify(productoRepository).save(producto);
    }

    @Test
    void shouldUpdateProducto() {
        Producto updated = new Producto();
        updated.setNombre("Paracetamol");
        updated.setMarca("Genérico");
        updated.setPrecio(BigDecimal.valueOf(990));
        updated.setDescripcion("Fiebre");
        updated.setImagenRepresentativaURL("http://img.com/para.jpg");
        updated.setCategoria("Medicamento");
        updated.setPorcentajeConcentracion(0.3);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(updated);

        ProductoDTO dto = productoService.updateById(1L, updated);
        assertThat(dto.getNombre()).isEqualTo("Paracetamol");
        verify(productoRepository).findById(1L);
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdateProductoNotFound() {
        when(productoRepository.findById(2L)).thenReturn(Optional.empty());
        Producto updated = new Producto();
        assertThatThrownBy(() -> productoService.updateById(2L, updated))
                .isInstanceOf(ProductoException.class)
                .hasMessageContaining("no existe");
        verify(productoRepository).findById(2L);
    }

    @Test
    void shouldDeleteProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        doNothing().when(productoRepository).deleteById(1L);
        productoService.deleteById(1L);
        verify(productoRepository).findById(1L);
        verify(productoRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeleteProductoNotFound() {
        when(productoRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> productoService.deleteById(2L))
                .isInstanceOf(ProductoException.class)
                .hasMessageContaining("no existe");
        verify(productoRepository).findById(2L);
    }

    @Test
    void shouldConvertToDTO() {
        ProductoDTO dto = productoService.convertToDTO(producto);
        assertThat(dto.getNombre()).isEqualTo("Aspirina");
        assertThat(dto.getMarca()).isEqualTo("Bayer");
        assertThat(dto.getPrecio()).isEqualTo(BigDecimal.valueOf(1990));
    }
} 