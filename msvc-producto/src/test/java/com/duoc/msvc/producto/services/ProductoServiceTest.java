package com.duoc.msvc.producto.services;

import com.duoc.msvc.producto.exceptions.ProductoException;
import com.duoc.msvc.producto.models.entities.Producto;
import com.duoc.msvc.producto.repositories.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

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
        producto.setNombre("Perfume");
        producto.setMarca("Carolina Herrera");
        producto.setPrecio(BigDecimal.valueOf(1990));
        producto.setDescripcion("El mejor perfume del mercado");
        producto.setImagenRepresentativaURL("http://img.com/perfume.jpg");
        producto.setCategoria("Aromatico");
        producto.setPorcentajeConcentracion(0.5);
    }

    @Test
    void shouldListAllProductos() {
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto));
        
        CollectionModel<EntityModel<Producto>> result = productoService.findAll();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().iterator().next().getContent().getNombre()).isEqualTo("Perfume");
        verify(productoRepository).findAll();
    }

    @Test
    void shouldFindProductoById() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        
        EntityModel<Producto> result = productoService.findById(1L);
        assertThat(result.getContent().getNombre()).isEqualTo("Perfume");
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
        
        EntityModel<Producto> result = productoService.save(producto);
        assertThat(result.getContent().getNombre()).isEqualTo("Perfume");
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

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(updated);

        EntityModel<Producto> result = productoService.updateById(1L, updated);
        assertThat(result.getContent().getNombre()).isEqualTo("OtroPerfume");
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
    void shouldFindProductosByCategoria() {
        Producto producto2 = new Producto();
        producto2.setIdProducto(2L);
        producto2.setNombre("Colonia");
        producto2.setCategoria("Aromatico");
        
        when(productoRepository.findByCategoria("Aromatico")).thenReturn(Arrays.asList(producto, producto2));
        
        CollectionModel<EntityModel<Producto>> result = productoService.findByCategoria("Aromatico");
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().iterator().next().getContent().getCategoria()).isEqualTo("Aromatico");
        verify(productoRepository).findByCategoria("Aromatico");
    }

    @Test
    void shouldFindProductosByMarca() {
        Producto producto2 = new Producto();
        producto2.setIdProducto(2L);
        producto2.setNombre("Colonia");
        producto2.setMarca("Carolina Herrera");
        
        when(productoRepository.findByMarca("Carolina Herrera")).thenReturn(Arrays.asList(producto, producto2));
        
        CollectionModel<EntityModel<Producto>> result = productoService.findByMarca("Carolina Herrera");
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().iterator().next().getContent().getMarca()).isEqualTo("Carolina Herrera");
        verify(productoRepository).findByMarca("Carolina Herrera");
    }

    @Test
    void shouldReturnEmptyListWhenCategoriaNotFound() {
        when(productoRepository.findByCategoria("Inexistente")).thenReturn(Arrays.asList());
        
        CollectionModel<EntityModel<Producto>> result = productoService.findByCategoria("Inexistente");
        assertThat(result.getContent()).isEmpty();
        verify(productoRepository).findByCategoria("Inexistente");
    }

    @Test
    void shouldReturnEmptyListWhenMarcaNotFound() {
        when(productoRepository.findByMarca("Marca Inexistente")).thenReturn(Arrays.asList());
        
        CollectionModel<EntityModel<Producto>> result = productoService.findByMarca("Marca Inexistente");
        assertThat(result.getContent()).isEmpty();
        verify(productoRepository).findByMarca("Marca Inexistente");
    }

    @Test
    void shouldCreateProductoWithNullValues() {
        Producto productoNull = new Producto();
        productoNull.setIdProducto(3L);
        productoNull.setNombre(null);
        productoNull.setMarca(null);
        productoNull.setPrecio(null);
        
        when(productoRepository.save(any(Producto.class))).thenReturn(productoNull);
        
        EntityModel<Producto> result = productoService.save(productoNull);
        assertThat(result.getContent().getIdProducto()).isEqualTo(3L);
        verify(productoRepository).save(productoNull);
    }

    @Test
    void shouldUpdateProductoWithPartialData() {
        Producto partialUpdate = new Producto();
        partialUpdate.setNombre("Nuevo Nombre");
        partialUpdate.setPrecio(BigDecimal.valueOf(2500));
        
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        
        EntityModel<Producto> result = productoService.updateById(1L, partialUpdate);
        assertThat(result.getContent().getNombre()).isEqualTo("Nuevo Nombre");
        verify(productoRepository).findById(1L);
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void shouldValidateProductoEntityModelStructure() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        
        EntityModel<Producto> entityModel = productoService.findById(1L);
        
        assertThat(entityModel).isNotNull();
        assertThat(entityModel.getContent()).isNotNull();
        assertThat(entityModel.getContent().getIdProducto()).isEqualTo(1L);
        assertThat(entityModel.getContent().getNombre()).isEqualTo("Perfume");
        assertThat(entityModel.getContent().getMarca()).isEqualTo("Carolina Herrera");
        assertThat(entityModel.getContent().getPrecio()).isEqualTo(BigDecimal.valueOf(1990));
        assertThat(entityModel.getContent().getDescripcion()).isEqualTo("El mejor perfume del mercado");
        assertThat(entityModel.getContent().getImagenRepresentativaURL()).isEqualTo("http://img.com/perfume.jpg");
        assertThat(entityModel.getContent().getCategoria()).isEqualTo("Aromatico");
        assertThat(entityModel.getContent().getPorcentajeConcentracion()).isEqualTo(0.5);
        
        verify(productoRepository).findById(1L);
    }

    @Test
    void shouldValidateCollectionModelStructure() {
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto));
        
        CollectionModel<EntityModel<Producto>> collectionModel = productoService.findAll();
        
        assertThat(collectionModel).isNotNull();
        assertThat(collectionModel.getContent()).hasSize(1);
        assertThat(collectionModel.getLinks()).isNotEmpty();
        
        verify(productoRepository).findAll();
    }

    @Test
    void shouldHandleMultipleProductosInCollection() {
        Producto producto2 = new Producto();
        producto2.setIdProducto(2L);
        producto2.setNombre("Colonia");
        producto2.setMarca("Chanel");
        producto2.setPrecio(BigDecimal.valueOf(3000));
        
        Producto producto3 = new Producto();
        producto3.setIdProducto(3L);
        producto3.setNombre("Aceite");
        producto3.setMarca("L'Occitane");
        producto3.setPrecio(BigDecimal.valueOf(1500));
        
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto, producto2, producto3));
        
        CollectionModel<EntityModel<Producto>> result = productoService.findAll();
        assertThat(result.getContent()).hasSize(3);
        
        verify(productoRepository).findAll();
    }

    @Test
    void shouldUpdateAllProductoFields() {
        Producto updated = new Producto();
        updated.setNombre("Nuevo Perfume");
        updated.setMarca("Nueva Marca");
        updated.setPrecio(BigDecimal.valueOf(5000));
        updated.setDescripcion("Nueva descripción");
        updated.setImagenRepresentativaURL("http://img.com/nuevo.jpg");
        updated.setCategoria("Nueva Categoría");
        updated.setPorcentajeConcentracion(0.8);
        
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(updated);
        
        EntityModel<Producto> result = productoService.updateById(1L, updated);
        
        assertThat(result.getContent().getNombre()).isEqualTo("Nuevo Perfume");
        assertThat(result.getContent().getMarca()).isEqualTo("Nueva Marca");
        assertThat(result.getContent().getPrecio()).isEqualTo(BigDecimal.valueOf(5000));
        assertThat(result.getContent().getDescripcion()).isEqualTo("Nueva descripción");
        assertThat(result.getContent().getImagenRepresentativaURL()).isEqualTo("http://img.com/nuevo.jpg");
        assertThat(result.getContent().getCategoria()).isEqualTo("Nueva Categoría");
        assertThat(result.getContent().getPorcentajeConcentracion()).isEqualTo(0.8);
        
        verify(productoRepository).findById(1L);
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void shouldHandleZeroPrecio() {
        producto.setPrecio(BigDecimal.ZERO);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        
        EntityModel<Producto> result = productoService.save(producto);
        assertThat(result.getContent().getPrecio()).isEqualTo(BigDecimal.ZERO);
        verify(productoRepository).save(producto);
    }

    @Test
    void shouldHandleNegativePrecio() {
        producto.setPrecio(BigDecimal.valueOf(-100));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        
        EntityModel<Producto> result = productoService.save(producto);
        assertThat(result.getContent().getPrecio()).isEqualTo(BigDecimal.valueOf(-100));
        verify(productoRepository).save(producto);
    }

    @Test
    void shouldHandleEmptyStringValues() {
        producto.setNombre("");
        producto.setMarca("");
        producto.setDescripcion("");
        producto.setCategoria("");
        producto.setImagenRepresentativaURL("");
        
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        
        EntityModel<Producto> result = productoService.save(producto);
        assertThat(result.getContent().getNombre()).isEqualTo("");
        assertThat(result.getContent().getMarca()).isEqualTo("");
        verify(productoRepository).save(producto);
    }

    @Test
    void shouldHandleSpecialCharactersInStrings() {
        producto.setNombre("Perfume & Colonia");
        producto.setMarca("L'Oréal");
        producto.setDescripcion("Descripción con símbolos: @#$%");
        producto.setCategoria("Aromático-Floral");
        
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        
        EntityModel<Producto> result = productoService.save(producto);
        assertThat(result.getContent().getNombre()).isEqualTo("Perfume & Colonia");
        assertThat(result.getContent().getMarca()).isEqualTo("L'Oréal");
        assertThat(result.getContent().getDescripcion()).isEqualTo("Descripción con símbolos: @#$%");
        assertThat(result.getContent().getCategoria()).isEqualTo("Aromático-Floral");
        verify(productoRepository).save(producto);
    }

    @Test
    void shouldHandleLargePrecioValues() {
        producto.setPrecio(BigDecimal.valueOf(999999.99));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        
        EntityModel<Producto> result = productoService.save(producto);
        assertThat(result.getContent().getPrecio()).isEqualTo(BigDecimal.valueOf(999999.99));
        verify(productoRepository).save(producto);
    }

    @Test
    void shouldHandlePorcentajeConcentracionBoundaries() {
        producto.setPorcentajeConcentracion(0.0);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        
        EntityModel<Producto> result = productoService.save(producto);
        assertThat(result.getContent().getPorcentajeConcentracion()).isEqualTo(0.0);
        verify(productoRepository).save(producto);
    }

    @Test
    void shouldHandleMaxPorcentajeConcentracion() {
        producto.setPorcentajeConcentracion(1.0);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        
        EntityModel<Producto> result = productoService.save(producto);
        assertThat(result.getContent().getPorcentajeConcentracion()).isEqualTo(1.0);
        verify(productoRepository).save(producto);
    }

    @Test
    void shouldValidateFindByCategoriaWithSpecialCharacters() {
        when(productoRepository.findByCategoria("Aromático-Floral")).thenReturn(Arrays.asList(producto));
        
        CollectionModel<EntityModel<Producto>> result = productoService.findByCategoria("Aromático-Floral");
        assertThat(result.getContent()).hasSize(1);
        verify(productoRepository).findByCategoria("Aromático-Floral");
    }

    @Test
    void shouldValidateFindByMarcaWithSpecialCharacters() {
        when(productoRepository.findByMarca("L'Oréal")).thenReturn(Arrays.asList(producto));
        
        CollectionModel<EntityModel<Producto>> result = productoService.findByMarca("L'Oréal");
        assertThat(result.getContent()).hasSize(1);
        verify(productoRepository).findByMarca("L'Oréal");
    }

    @Test
    void shouldHandleRepositoryExceptionGracefully() {
        when(productoRepository.findById(1L)).thenThrow(new RuntimeException("Error de base de datos"));
        
        assertThatThrownBy(() -> productoService.findById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error de base de datos");
        
        verify(productoRepository).findById(1L);
    }

    @Test
    void shouldValidateEntityModelStructure() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        
        EntityModel<Producto> entityModel = productoService.findById(1L);
        
        assertThat(entityModel).isNotNull();
        assertThat(entityModel.getContent()).isNotNull();
        assertThat(entityModel.getContent().getIdProducto()).isEqualTo(1L);
        assertThat(entityModel.getContent().getNombre()).isEqualTo("Perfume");
        assertThat(entityModel.getContent().getMarca()).isEqualTo("Carolina Herrera");
        assertThat(entityModel.getContent().getPrecio()).isEqualTo(BigDecimal.valueOf(1990));
        assertThat(entityModel.getContent().getDescripcion()).isEqualTo("El mejor perfume del mercado");
        assertThat(entityModel.getContent().getImagenRepresentativaURL()).isEqualTo("http://img.com/perfume.jpg");
        assertThat(entityModel.getContent().getCategoria()).isEqualTo("Aromatico");
        assertThat(entityModel.getContent().getPorcentajeConcentracion()).isEqualTo(0.5);
        
        verify(productoRepository).findById(1L);
    }
} 