package com.duoc.msvc.pedido.services;

import com.duoc.msvc.pedido.dtos.PedidoHateoasDTO;
import com.duoc.msvc.pedido.dtos.pojos.SucursalClientDTO;
import com.duoc.msvc.pedido.exceptions.PedidoException;
import com.duoc.msvc.pedido.models.entities.Pedido;
import com.duoc.msvc.pedido.models.entities.DetallePedido;
import com.duoc.msvc.pedido.repositories.PedidoRepository;
import com.duoc.msvc.pedido.clients.*;
import com.duoc.msvc.pedido.dtos.pojos.ProductoClientDTO;
import com.duoc.msvc.pedido.dtos.pojos.UsuarioClientDTO;
import com.duoc.msvc.pedido.dtos.pojos.InventarioClientDTO;
import com.duoc.msvc.pedido.dtos.pojos.EnvioClientDTO;
import com.duoc.msvc.pedido.dtos.pojos.PagoClientDTO;
import com.duoc.msvc.pedido.assemblers.PedidoDTOModelAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private UsuarioClient usuarioClient;
    @Mock
    private ProductoClient productoClient;
    @Mock
    private EnvioClient envioClient;
    @Mock
    private PagoClient pagoClient;
    @Mock
    private SucursalClient sucursalClient;

    private PedidoDTOModelAssembler assembler;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private Pedido pedido;
    private DetallePedido detalle;

    @BeforeEach
    void setUp() throws Exception {
        assembler = new PedidoDTOModelAssembler();
        Field assemblerField = PedidoServiceImpl.class.getDeclaredField("assembler");
        assemblerField.setAccessible(true);
        assemblerField.set(pedidoService, assembler);
        pedido = new Pedido();
        pedido.setIdPedido(1L);
        pedido.setIdCliente(10L);
        pedido.setMetodoPago("Tarjeta");
        pedido.setCostoEnvio(BigDecimal.valueOf(2000));
        pedido.setTotalDetalles(BigDecimal.valueOf(10000));
        pedido.setMontoFinal(BigDecimal.valueOf(12000));
        pedido.setEstado("Nuevo");
        detalle = new DetallePedido();
        detalle.setIdDetallePedido(1L);
        detalle.setIdProducto(5L);
        detalle.setIdSucursal(2L);
        detalle.setCantidad(2);
        detalle.setPrecio(BigDecimal.valueOf(5000));
        detalle.setPedido(pedido);
        pedido.setDetallesPedido(Collections.singletonList(detalle));
    }

    @Test
    void shouldListAllPedidos() {
        when(pedidoRepository.findAllWithDetalles()).thenReturn(List.of(pedido));
        // Mock necesario para usuarioClient
        UsuarioClientDTO mockUsuario = new UsuarioClientDTO();
        mockUsuario.setIdCliente(10L);
        mockUsuario.setNombre("Juan");
        mockUsuario.setApellido("Pérez");
        mockUsuario.setCiudad("Santiago");
        mockUsuario.setRegion("Metropolitana");
        mockUsuario.setComuna("Providencia");
        mockUsuario.setCodigoPostal("7500000");
        mockUsuario.setDireccion("Av. Providencia 123");
        mockUsuario.setCorreo("juan.perez@email.com");
        when(usuarioClient.getUsuarioById(10L)).thenReturn(mockUsuario);
        // Mock para productoClient
        ProductoClientDTO mockProducto = new ProductoClientDTO();
        mockProducto.setNombre("Producto Test");
        mockProducto.setMarca("Marca Test");
        when(productoClient.findById(5L)).thenReturn(mockProducto);
        CollectionModel<PedidoHateoasDTO> result = pedidoService.findAll();
        assertThat(result.getContent()).hasSize(1);
        PedidoHateoasDTO dto = result.getContent().iterator().next();
        assertThat(dto.getIdCliente()).isEqualTo(10L);
        assertThat(dto.getFecha()).isNotNull();
        verify(pedidoRepository).findAllWithDetalles();
    }

    @Test
    void shouldFindPedidoById() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        
        // Mock para usuarioClient.getUsuarioById que se llama en convertToDTO
        UsuarioClientDTO mockUsuario = new UsuarioClientDTO();
        mockUsuario.setIdCliente(10L);
        mockUsuario.setNombre("Juan");
        mockUsuario.setApellido("Pérez");
        mockUsuario.setCiudad("Santiago");
        mockUsuario.setRegion("Metropolitana");
        mockUsuario.setComuna("Providencia");
        mockUsuario.setCodigoPostal("7500000");
        mockUsuario.setDireccion("Av. Providencia 123");
        mockUsuario.setCorreo("juan.perez@email.com");
        when(usuarioClient.getUsuarioById(10L)).thenReturn(mockUsuario);
        
        // Mock para productoClient.findById que se llama en convertToDTO
        ProductoClientDTO mockProducto = new ProductoClientDTO();
        mockProducto.setNombre("Producto Test");
        mockProducto.setMarca("Marca Test");
        when(productoClient.findById(5L)).thenReturn(mockProducto);
        
        PedidoHateoasDTO dto = pedidoService.findById(1L);
        assertThat(dto.getIdCliente()).isEqualTo(10L);
        assertThat(dto.getFecha()).isNotNull();
        verify(pedidoRepository).findById(1L);
        verify(usuarioClient).getUsuarioById(10L);
        verify(productoClient).findById(5L);
    }

    @Test
    void shouldFindAllByIdCliente() {
        when(pedidoRepository.findByIdCliente(10L)).thenReturn(List.of(pedido));
        // Mock necesario para usuarioClient
        UsuarioClientDTO mockUsuario = new UsuarioClientDTO();
        mockUsuario.setIdCliente(10L);
        mockUsuario.setNombre("Juan");
        mockUsuario.setApellido("Pérez");
        mockUsuario.setCiudad("Santiago");
        mockUsuario.setRegion("Metropolitana");
        mockUsuario.setComuna("Providencia");
        mockUsuario.setCodigoPostal("7500000");
        mockUsuario.setDireccion("Av. Providencia 123");
        mockUsuario.setCorreo("juan.perez@email.com");
        when(usuarioClient.getUsuarioById(10L)).thenReturn(mockUsuario);
        // Mock para productoClient
        ProductoClientDTO mockProducto = new ProductoClientDTO();
        mockProducto.setNombre("Producto Test");
        mockProducto.setMarca("Marca Test");
        when(productoClient.findById(5L)).thenReturn(mockProducto);
        CollectionModel<PedidoHateoasDTO> result = pedidoService.findByIdCliente(10L);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().iterator().next().getIdCliente()).isEqualTo(10L);
        verify(pedidoRepository).findByIdCliente(10L);
    }

    @Test
    void shouldThrowExceptionWhenPedidoNotFound() {
        when(pedidoRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> pedidoService.findById(2L))
                .isInstanceOf(PedidoException.class)
                .hasMessageContaining("no existe");
        verify(pedidoRepository).findById(2L);
    }

    @Test
    void shouldCreatePedido() {
        ProductoClientDTO mockProducto = new ProductoClientDTO();
        mockProducto.setPrecio(BigDecimal.valueOf(10000));
        when(productoClient.findById(5L)).thenReturn(mockProducto);

        SucursalClientDTO mockSucursal = new SucursalClientDTO();
        InventarioClientDTO mockInventario = new InventarioClientDTO();
        mockInventario.setIdProducto(5L);
        mockInventario.setIdInventario(5L);
        mockInventario.setStock(10);
        mockSucursal.setInventarios(List.of(mockInventario));
        mockSucursal.setId(2L);
        when(sucursalClient.getSucursalBestStockByIdProducto(5L)).thenReturn(mockSucursal);

        when(envioClient.getCostoEnvio()).thenReturn(BigDecimal.valueOf(5000));

        UsuarioClientDTO mockUsuario = new UsuarioClientDTO();
        mockUsuario.setIdCliente(10L);
        mockUsuario.setNombre("Juan");
        mockUsuario.setApellido("Pérez");
        mockUsuario.setCiudad("Santiago");
        mockUsuario.setRegion("Metropolitana");
        mockUsuario.setComuna("Providencia");
        mockUsuario.setCodigoPostal("7500000");
        mockUsuario.setDireccion("Av. Providencia 123");
        mockUsuario.setCorreo("juan.perez@email.com");
        when(usuarioClient.getUsuarioById(10L)).thenReturn(mockUsuario);

        EnvioClientDTO mockEnvio = new EnvioClientDTO();
        mockEnvio.setCosto(BigDecimal.valueOf(5000));
        when(envioClient.save(any())).thenReturn(mockEnvio);

        // Mock para pagoClient.save que retorna PagoClientDTO
        PagoClientDTO mockPago = new PagoClientDTO();
        mockPago.setIdPedido(1L);
        mockPago.setMonto(BigDecimal.valueOf(25000));
        mockPago.setMetodoPago("Tarjeta");
        when(pagoClient.save(any())).thenReturn(mockPago);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        PedidoHateoasDTO dto = pedidoService.save(pedido);

        assertThat(dto).isNotNull();
        assertThat(dto.getTotalDetalles()).isEqualTo(new BigDecimal("20000"));
        assertThat(dto.getMontoFinal()).isEqualTo(new BigDecimal("25000"));
        verify(pedidoRepository).save(any(Pedido.class));
        verify(productoClient, times(2)).findById(5L);
        verify(sucursalClient).getSucursalBestStockByIdProducto(5L);
        verify(sucursalClient).updateStock(eq(2L), eq(5L), anyInt());
        verify(envioClient).getCostoEnvio();
        verify(usuarioClient, times(2)).getUsuarioById(10L);
        verify(pagoClient).save(any());
        verify(envioClient).save(any(EnvioClientDTO.class));
    }

    @Test
    void shouldUpdatePedido() {
        // Arrange
        when(pedidoRepository.findById(anyLong())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // Configurar los mocks de los clientes que son llamados por convertToDTO
        UsuarioClientDTO mockUsuario = new UsuarioClientDTO();
        mockUsuario.setNombre("Cliente");
        mockUsuario.setApellido("Actualizado");
        when(usuarioClient.getUsuarioById(anyLong())).thenReturn(mockUsuario);

        ProductoClientDTO mockProducto = new ProductoClientDTO();
        mockProducto.setNombre("Producto");
        mockProducto.setMarca("Actualizado");
        when(productoClient.findById(anyLong())).thenReturn(mockProducto);

        Pedido pedidoActualizado = new Pedido();
        pedidoActualizado.setEstado("Enviado");

        // Act
        PedidoHateoasDTO dto = pedidoService.updateById(1L, pedidoActualizado);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getEstado()).isEqualTo("Enviado");
        assertThat(dto.getNombreCliente()).isEqualTo("Cliente");
        assertThat(dto.getApellidoCliente()).isEqualTo("Actualizado");
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatePedidoNotFound() {
        when(pedidoRepository.findById(2L)).thenReturn(Optional.empty());
        Pedido updated = new Pedido();
        assertThatThrownBy(() -> pedidoService.updateById(2L, updated))
                .isInstanceOf(PedidoException.class)
                .hasMessageContaining("no existe");
        verify(pedidoRepository).findById(2L);
    }

    @Test
    void shouldDeletePedido() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        doNothing().when(pedidoRepository).deleteById(1L);
        pedidoService.deleteById(1L);
        verify(pedidoRepository).findById(1L);
        verify(pedidoRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletePedidoNotFound() {
        when(pedidoRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> pedidoService.deleteById(2L))
                .isInstanceOf(PedidoException.class)
                .hasMessageContaining("no existe");
        verify(pedidoRepository).findById(2L);
    }

} 