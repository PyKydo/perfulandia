package com.duoc.msvc.pedido.services;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private Pedido pedido;
    private DetallePedido detalle;

    @BeforeEach
    void setUp() {
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
        
        CollectionModel<EntityModel<Pedido>> result = pedidoService.findAll();
        assertThat(result.getContent()).hasSize(1);
        EntityModel<Pedido> entityModel = result.getContent().iterator().next();
        Pedido pedidoResult = entityModel.getContent();
        assertThat(pedidoResult.getIdCliente()).isEqualTo(10L);
        assertThat(pedidoResult.getFecha()).isNotNull();
        verify(pedidoRepository).findAllWithDetalles();
    }

    @Test
    void shouldFindPedidoById() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        
        EntityModel<Pedido> entityModel = pedidoService.findById(1L);
        Pedido pedidoResult = entityModel.getContent();
        assertThat(pedidoResult.getIdCliente()).isEqualTo(10L);
        assertThat(pedidoResult.getFecha()).isNotNull();
        verify(pedidoRepository).findById(1L);
    }

    @Test
    void shouldFindAllByIdCliente() {
        when(pedidoRepository.findByIdCliente(10L)).thenReturn(List.of(pedido));
        
        CollectionModel<EntityModel<Pedido>> result = pedidoService.findByIdCliente(10L);
        assertThat(result.getContent()).hasSize(1);
        EntityModel<Pedido> entityModel = result.getContent().iterator().next();
        assertThat(entityModel.getContent().getIdCliente()).isEqualTo(10L);
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
        PagoClientDTO mockPago = new PagoClientDTO();
        mockPago.setIdPedido(1L);
        mockPago.setMonto(BigDecimal.valueOf(25000));
        mockPago.setMetodoPago("Tarjeta");
        when(pagoClient.save(any())).thenReturn(mockPago);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        EntityModel<Pedido> entityModel = pedidoService.save(pedido);
        Pedido pedidoResult = entityModel.getContent();

        assertThat(pedidoResult).isNotNull();
        assertThat(pedidoResult.getTotalDetalles()).isEqualTo(new BigDecimal("20000"));
        assertThat(pedidoResult.getMontoFinal()).isEqualTo(new BigDecimal("25000"));
        verify(pedidoRepository).save(any(Pedido.class));
        verify(productoClient).findById(5L);
        verify(sucursalClient).getSucursalBestStockByIdProducto(5L);
        verify(sucursalClient).updateStock(eq(2L), eq(5L), anyInt());
        verify(envioClient).getCostoEnvio();
        verify(usuarioClient).getUsuarioById(10L);
        verify(pagoClient).save(any());
        verify(envioClient).save(any(EnvioClientDTO.class));
    }

    @Test
    void shouldUpdatePedido() {
        when(pedidoRepository.findById(anyLong())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido pedidoActualizado = new Pedido();
        pedidoActualizado.setEstado("Enviado");
        EntityModel<Pedido> entityModel = pedidoService.updateById(1L, pedidoActualizado);
        Pedido pedidoResult = entityModel.getContent();
        assertThat(pedidoResult).isNotNull();
        assertThat(pedidoResult.getEstado()).isEqualTo("Enviado");
        verify(pedidoRepository).findById(1L);
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatePedidoNotFound() {
        when(pedidoRepository.findById(2L)).thenReturn(Optional.empty());
        Pedido pedidoActualizado = new Pedido();
        assertThatThrownBy(() -> pedidoService.updateById(2L, pedidoActualizado))
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

    @Test
    void shouldThrowExceptionWhenStockInsufficient() {
        ProductoClientDTO mockProducto = new ProductoClientDTO();
        mockProducto.setPrecio(BigDecimal.valueOf(10000));
        when(productoClient.findById(5L)).thenReturn(mockProducto);

        SucursalClientDTO mockSucursal = new SucursalClientDTO();
        InventarioClientDTO mockInventario = new InventarioClientDTO();
        mockInventario.setIdProducto(5L);
        mockInventario.setIdInventario(5L);
        mockInventario.setStock(1); // Stock insuficiente para cantidad 2
        mockSucursal.setInventarios(List.of(mockInventario));
        mockSucursal.setId(2L);
        when(sucursalClient.getSucursalBestStockByIdProducto(5L)).thenReturn(mockSucursal);

        assertThatThrownBy(() -> pedidoService.save(pedido))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Stock insuficiente");
    }

    @Test
    void shouldThrowExceptionWhenStockZero() {
        ProductoClientDTO mockProducto = new ProductoClientDTO();
        mockProducto.setPrecio(BigDecimal.valueOf(10000));
        when(productoClient.findById(5L)).thenReturn(mockProducto);

        SucursalClientDTO mockSucursal = new SucursalClientDTO();
        InventarioClientDTO mockInventario = new InventarioClientDTO();
        mockInventario.setIdProducto(5L);
        mockInventario.setIdInventario(5L);
        mockInventario.setStock(0); // Stock cero
        mockSucursal.setInventarios(List.of(mockInventario));
        mockSucursal.setId(2L);
        when(sucursalClient.getSucursalBestStockByIdProducto(5L)).thenReturn(mockSucursal);

        assertThatThrownBy(() -> pedidoService.save(pedido))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Stock insuficiente");
    }

    @Test
    void shouldUpdateStockCorrectly() {
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
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

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

        PagoClientDTO mockPago = new PagoClientDTO();
        mockPago.setIdPedido(1L);
        mockPago.setMonto(BigDecimal.valueOf(25000));
        mockPago.setMetodoPago("Tarjeta");
        when(pagoClient.save(any())).thenReturn(mockPago);

        pedidoService.save(pedido);
        verify(sucursalClient).updateStock(eq(2L), eq(5L), eq(8));
    }

    @Test
    void shouldCalculateTotalsWithMultipleProducts() {
        Pedido pedidoMultiple = new Pedido();
        pedidoMultiple.setIdPedido(1L);
        pedidoMultiple.setIdCliente(10L);
        pedidoMultiple.setMetodoPago("Tarjeta");
        pedidoMultiple.setEstado("Nuevo");

        DetallePedido detalle1 = new DetallePedido();
        detalle1.setIdProducto(5L);
        detalle1.setCantidad(2);
        detalle1.setPedido(pedidoMultiple);

        DetallePedido detalle2 = new DetallePedido();
        detalle2.setIdProducto(6L);
        detalle2.setCantidad(1);
        detalle2.setPedido(pedidoMultiple);

        pedidoMultiple.setDetallesPedido(List.of(detalle1, detalle2));
        ProductoClientDTO mockProducto1 = new ProductoClientDTO();
        mockProducto1.setPrecio(BigDecimal.valueOf(10000));
        when(productoClient.findById(5L)).thenReturn(mockProducto1);
        ProductoClientDTO mockProducto2 = new ProductoClientDTO();
        mockProducto2.setPrecio(BigDecimal.valueOf(15000));
        when(productoClient.findById(6L)).thenReturn(mockProducto2);
        SucursalClientDTO mockSucursal1 = new SucursalClientDTO();
        InventarioClientDTO mockInventario1 = new InventarioClientDTO();
        mockInventario1.setIdProducto(5L);
        mockInventario1.setIdInventario(5L);
        mockInventario1.setStock(10);
        mockSucursal1.setInventarios(List.of(mockInventario1));
        mockSucursal1.setId(2L);
        when(sucursalClient.getSucursalBestStockByIdProducto(5L)).thenReturn(mockSucursal1);
        SucursalClientDTO mockSucursal2 = new SucursalClientDTO();
        InventarioClientDTO mockInventario2 = new InventarioClientDTO();
        mockInventario2.setIdProducto(6L);
        mockInventario2.setIdInventario(6L);
        mockInventario2.setStock(5);
        mockSucursal2.setInventarios(List.of(mockInventario2));
        mockSucursal2.setId(3L);
        when(sucursalClient.getSucursalBestStockByIdProducto(6L)).thenReturn(mockSucursal2);

        when(envioClient.getCostoEnvio()).thenReturn(BigDecimal.valueOf(5000));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoMultiple);

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

        PagoClientDTO mockPago = new PagoClientDTO();
        mockPago.setIdPedido(1L);
        mockPago.setMonto(BigDecimal.valueOf(40000));
        mockPago.setMetodoPago("Tarjeta");
        when(pagoClient.save(any())).thenReturn(mockPago);

        EntityModel<Pedido> result = pedidoService.save(pedidoMultiple);
        assertThat(result.getContent().getTotalDetalles()).isEqualTo(new BigDecimal("35000"));
        assertThat(result.getContent().getMontoFinal()).isEqualTo(new BigDecimal("40000"));
    }

    @Test
    void shouldCalculateShippingCostCorrectly() {
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

        when(envioClient.getCostoEnvio()).thenReturn(BigDecimal.valueOf(3000));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

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
        mockEnvio.setCosto(BigDecimal.valueOf(3000));
        when(envioClient.save(any())).thenReturn(mockEnvio);

        PagoClientDTO mockPago = new PagoClientDTO();
        mockPago.setIdPedido(1L);
        mockPago.setMonto(BigDecimal.valueOf(23000));
        mockPago.setMetodoPago("Tarjeta");
        when(pagoClient.save(any())).thenReturn(mockPago);

        EntityModel<Pedido> result = pedidoService.save(pedido);
        assertThat(result.getContent().getCostoEnvio()).isEqualTo(BigDecimal.valueOf(3000));
        assertThat(result.getContent().getMontoFinal()).isEqualTo(new BigDecimal("23000"));
    }

    @Test
    void shouldUpdateEstadoToPagado() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(pagoClient.updateEstadoById(1L, "Completado")).thenReturn("Estado actualizado");

        String result = pedidoService.updateEstadoById(1L, "Pagado");

        assertThat(result).isEqualTo("Pagado");
        verify(pagoClient).updateEstadoById(1L, "Completado");
    }

    @Test
    void shouldUpdateEstadoToCancelado() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(pagoClient.updateEstadoById(1L, "Cancelado")).thenReturn("Estado actualizado");

        String result = pedidoService.updateEstadoById(1L, "Cancelado");

        assertThat(result).isEqualTo("Cancelado");
        verify(pagoClient).updateEstadoById(1L, "Cancelado");
    }

    @Test
    void shouldThrowExceptionWhenEstadoVacio() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        assertThatThrownBy(() -> pedidoService.updateEstadoById(1L, ""))
                .isInstanceOf(PedidoException.class)
                .hasMessageContaining("no puede estar vacío");
    }

    @Test
    void shouldThrowExceptionWhenEstadoNulo() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        assertThatThrownBy(() -> pedidoService.updateEstadoById(1L, null))
                .isInstanceOf(PedidoException.class)
                .hasMessageContaining("no puede estar vacío");
    }

    @Test
    void shouldThrowExceptionWhenPedidoAlreadyPagado() {
        pedido.setEstado("Pagado");
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        assertThatThrownBy(() -> pedidoService.updateEstadoById(1L, "Pagado"))
                .isInstanceOf(PedidoException.class)
                .hasMessageContaining("ya está pagado");
    }

    @Test
    void shouldHandleProductoClientFailure() {
        when(productoClient.findById(5L)).thenThrow(new RuntimeException("Producto service unavailable"));

        assertThatThrownBy(() -> pedidoService.save(pedido))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Producto service unavailable");
    }

    @Test
    void shouldHandleSucursalClientFailure() {
        ProductoClientDTO mockProducto = new ProductoClientDTO();
        mockProducto.setPrecio(BigDecimal.valueOf(10000));
        when(productoClient.findById(5L)).thenReturn(mockProducto);

        when(sucursalClient.getSucursalBestStockByIdProducto(5L))
                .thenThrow(new RuntimeException("Sucursal service unavailable"));

        assertThatThrownBy(() -> pedidoService.save(pedido))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Sucursal service unavailable");
    }

    @Test
    void shouldHandleEnvioClientFailure() {
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

        when(envioClient.getCostoEnvio()).thenThrow(new RuntimeException("Envio service unavailable"));

        assertThatThrownBy(() -> pedidoService.save(pedido))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Envio service unavailable");
    }

    @Test
    void shouldHandlePagoClientFailure() {
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
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        when(pagoClient.save(any())).thenThrow(new RuntimeException("Pago service unavailable"));

        assertThatThrownBy(() -> pedidoService.save(pedido))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Pago service unavailable");
    }

    @Test
    void shouldThrowExceptionWhenPedidoNull() {
        assertThatThrownBy(() -> pedidoService.save(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no puede ser nulo");
    }

    @Test
    void shouldThrowExceptionWhenDetallesPedidoEmpty() {
        pedido.setDetallesPedido(Collections.emptyList());

        assertThatThrownBy(() -> pedidoService.save(pedido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("debe contener al menos un detalle");
    }

    @Test
    void shouldThrowExceptionWhenDetallesPedidoNull() {
        pedido.setDetallesPedido(null);

        assertThatThrownBy(() -> pedidoService.save(pedido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("debe contener al menos un detalle");
    }

    @Test
    void shouldThrowExceptionWhenMetodoPagoEmpty() {
        pedido.setMetodoPago("");

        assertThatThrownBy(() -> pedidoService.save(pedido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("método de pago válido");
    }

    @Test
    void shouldThrowExceptionWhenMetodoPagoNull() {
        pedido.setMetodoPago(null);

        assertThatThrownBy(() -> pedidoService.save(pedido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("método de pago válido");
    }

    @Test
    void shouldThrowExceptionWhenCantidadNegativa() {
        detalle.setCantidad(-1);

        assertThatThrownBy(() -> pedidoService.save(pedido))
            .isInstanceOf(PedidoException.class)
            .hasMessageContaining("La cantidad debe ser mayor a 0");
    }

    @Test
    void shouldThrowExceptionWhenCantidadCero() {
        detalle.setCantidad(0);
        pedido.setDetallesPedido(Collections.singletonList(detalle));
        
        assertThatThrownBy(() -> pedidoService.save(pedido))
                .isInstanceOf(PedidoException.class)
                .hasMessageContaining("La cantidad debe ser mayor a 0");
    }

    @Test
    void shouldConvertToDTO() {
        UsuarioClientDTO mockUsuario = new UsuarioClientDTO();
        mockUsuario.setNombre("Juan");
        mockUsuario.setApellido("Pérez");
        mockUsuario.setDireccion("Av. Providencia 123");
        mockUsuario.setRegion("Metropolitana");
        mockUsuario.setComuna("Providencia");
        mockUsuario.setCorreo("juan.perez@email.com");
        when(usuarioClient.getUsuarioById(10L)).thenReturn(mockUsuario);

        ProductoClientDTO mockProducto = new ProductoClientDTO();
        mockProducto.setNombre("Perfume");
        mockProducto.setMarca("Carolina Herrera");
        when(productoClient.findById(5L)).thenReturn(mockProducto);

        var dto = pedidoService.convertToDTO(pedido);
        
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getIdCliente()).isEqualTo(10L);
        assertThat(dto.getEstado()).isEqualTo("Nuevo");
        assertThat(dto.getMetodoPago()).isEqualTo("Tarjeta");
        assertThat(dto.getNombreCliente()).isEqualTo("Juan");
        assertThat(dto.getApellidoCliente()).isEqualTo("Pérez");
        assertThat(dto.getDireccion()).isEqualTo("Av. Providencia 123");
        assertThat(dto.getRegion()).isEqualTo("Metropolitana");
        assertThat(dto.getComuna()).isEqualTo("Providencia");
        assertThat(dto.getCorreo()).isEqualTo("juan.perez@email.com");
        assertThat(dto.getDetallesPedido()).isNotEmpty();
        assertThat(dto.getDetallesPedido().get(0).getNombreProducto()).isEqualTo("Perfume");
        assertThat(dto.getDetallesPedido().get(0).getMarcaProducto()).isEqualTo("Carolina Herrera");
        
        verify(usuarioClient).getUsuarioById(10L);
        verify(productoClient).findById(5L);
    }
} 