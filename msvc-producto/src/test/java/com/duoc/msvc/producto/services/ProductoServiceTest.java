package com.duoc.msvc.producto.services;

import com.duoc.msvc.producto.dtos.ProductoHateoasDTO;
import com.duoc.msvc.producto.exceptions.ProductoException;
import com.duoc.msvc.producto.models.entities.Producto;
import com.duoc.msvc.producto.repositories.ProductoRepository;
import com.duoc.msvc.producto.assemblers.ProductoDTOModelAssembler;
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
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private ProductoDTOModelAssembler assembler;
    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;
    private ProductoHateoasDTO productoHateoasDTO;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setIdProducto(1L);
        producto.setNombre("Perfume");
        producto.setMarca("Carolina Herrera");
        producto.setPrecio(BigDecimal.valueOf(1990));
        producto.setDescripcion("El mejor perfume del mercado");
        producto.setImagenRepresentativaURL("http://img.com/perfume.jpg");
        producto.setCategoria("Aromatico");
        producto.setPorcentajeConcentracion(0.5);

        productoHateoasDTO = new ProductoHateoasDTO();
        productoHateoasDTO.setId(1L);
        productoHateoasDTO.setNombre("Perfume");
        productoHateoasDTO.setMarca("Carolina Herrera");
        productoHateoasDTO.setPrecio(BigDecimal.valueOf(1990));
        productoHateoasDTO.setDescripcion("El mejor perfume del mercado");
        productoHateoasDTO.setImagenRepresentativaURL("http://img.com/Perfume.jpg");
        productoHateoasDTO.setCategoria("Aromatico");
        productoHateoasDTO.setPorcentajeConcentracion(0.5);
    }

    @Test
    void shouldListAllProductos() {
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto));
        when(assembler.toCollectionModel(Arrays.asList(producto))).thenReturn(CollectionModel.of(Arrays.asList(productoHateoasDTO)));
        
        CollectionModel<ProductoHateoasDTO> result = productoService.findAll();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().iterator().next().getNombre()).isEqualTo("Perfume");
        verify(productoRepository).findAll();
    }

    @Test
    void shouldFindProductoById() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(assembler.toModel(producto)).thenReturn(productoHateoasDTO);
        
        ProductoHateoasDTO dto = productoService.findById(1L);
        assertThat(dto.getNombre()).isEqualTo("Perfume");
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
        when(assembler.toModel(producto)).thenReturn(productoHateoasDTO);
        
        ProductoHateoasDTO dto = productoService.save(producto);
        assertThat(dto.getNombre()).isEqualTo("Perfume");
        verify(productoRepository).save(producto);
    }

    @Test
    void shouldUpdateProducto() {
        Producto updated = new Producto();
        updated.setNombre("OtroPerfume");
        updated.setMarca("OtraMarca");
        updated.setPrecio(BigDecimal.valueOf(990));
        updated.setDescripcion("OtraDescripcion");
        updated.setImagenRepresentativaURL("http://img.com/para.jpg");
        updated.setCategoria("OtraCategoria");
        updated.setPorcentajeConcentracion(0.3);

        ProductoHateoasDTO updatedHateoasDTO = new ProductoHateoasDTO();
        updatedHateoasDTO.setId(1L);
        updatedHateoasDTO.setNombre("OtroPerfume");
        updatedHateoasDTO.setMarca("OtraMarca");
        updatedHateoasDTO.setPrecio(BigDecimal.valueOf(990));
        updatedHateoasDTO.setDescripcion("OtraDescripcion");
        updatedHateoasDTO.setImagenRepresentativaURL("http://img.com/para.jpg");
        updatedHateoasDTO.setCategoria("OtraCategoria");
        updatedHateoasDTO.setPorcentajeConcentracion(0.3);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(updated);
        when(assembler.toModel(updated)).thenReturn(updatedHateoasDTO);

        ProductoHateoasDTO dto = productoService.updateById(1L, updated);
        assertThat(dto.getNombre()).isEqualTo("OtroPerfume");
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
} 