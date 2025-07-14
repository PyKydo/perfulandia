package com.duoc.msvc.usuario.services;

import com.duoc.msvc.usuario.clients.PedidoClient;
import com.duoc.msvc.usuario.dtos.pojos.PedidoClientDTO;
import com.duoc.msvc.usuario.dtos.pojos.DetallePedidoClientDTO;
import com.duoc.msvc.usuario.exceptions.UsuarioException;
import com.duoc.msvc.usuario.models.entities.Usuario;
import com.duoc.msvc.usuario.repositories.UsuarioRepository;
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
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.hateoas.EntityModel;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PedidoClient pedidoClient;
    @InjectMocks
    private UsuarioServicelmpl usuarioService;

    private Usuario usuario;
    private PedidoClientDTO pedidoClientDTO;
    private DetallePedidoClientDTO detallePedidoDTO;

    @BeforeEach
    void setUp() throws Exception {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setCorreo("juan.perez@mail.com");
        usuario.setTelefono("+56 9 1234 5678");
        usuario.setRegion("Región Metropolitana");
        usuario.setComuna("Santiago");
        usuario.setCiudad("Santiago");
        usuario.setDireccion("Calle Falsa 123");
        usuario.setCodigoPostal("1234567");

        detallePedidoDTO = new DetallePedidoClientDTO();
        detallePedidoDTO.setIdProducto(1L);
        detallePedidoDTO.setCantidad(2);
        detallePedidoDTO.setPrecio(new BigDecimal("15000"));

        pedidoClientDTO = new PedidoClientDTO();
        pedidoClientDTO.setIdCliente(1L);
        pedidoClientDTO.setDetallesPedido(Arrays.asList(detallePedidoDTO));
        pedidoClientDTO.setEstado("Pendiente");
    }

    @Test
    void shouldListAllUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));
        var result = usuarioService.findAll();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().iterator().next().getContent().getNombre()).isEqualTo("Juan");
        verify(usuarioRepository).findAll();
    }

    @Test
    void shouldFindUsuarioById() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        var result = usuarioService.findById(1L);
        assertThat(result.getContent().getNombre()).isEqualTo("Juan");
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUsuarioNotFound() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> usuarioService.findById(2L))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("no existe");
        verify(usuarioRepository).findById(2L);
    }

    @Test
    void shouldCreateUsuario() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        var result = usuarioService.save(usuario);
        assertThat(result.getContent().getNombre()).isEqualTo("Juan");
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void shouldUpdateUsuario() {
        Usuario updated = new Usuario();
        updated.setNombre("Pedro");
        updated.setApellido("Gómez");
        updated.setCorreo("pedro.gomez@mail.com");
        updated.setTelefono("+56 9 8765 4321");
        updated.setRegion("Valparaíso");
        updated.setComuna("Viña del Mar");
        updated.setCiudad("Viña del Mar");
        updated.setDireccion("Calle Nueva 456");
        updated.setCodigoPostal("7654321");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(updated);

        var result = usuarioService.updateById(1L, updated);
        assertThat(result.getContent().getNombre()).isEqualTo("Pedro");
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdateUsuarioNotFound() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());
        Usuario updated = new Usuario();
        assertThatThrownBy(() -> usuarioService.updateById(2L, updated))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("no existe");
        verify(usuarioRepository).findById(2L);
    }

    @Test
    void shouldDeleteUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).deleteById(1L);
        usuarioService.deleteById(1L);
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeleteUsuarioNotFound() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> usuarioService.deleteById(2L))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("no existe");
        verify(usuarioRepository).findById(2L);
    }

    @Test
    void shouldValidateEntityModelStructure() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        
        EntityModel<Usuario> entityModel = usuarioService.findById(1L);
        assertThat(entityModel).isNotNull();
        assertThat(entityModel.getContent()).isNotNull();
        assertThat(entityModel.getContent().getIdUsuario()).isEqualTo(1L);
        assertThat(entityModel.getContent().getNombre()).isEqualTo("Juan");
        assertThat(entityModel.getContent().getApellido()).isEqualTo("Pérez");
        
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void shouldRealizarPedidoSuccessfully() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(pedidoClient.save(any(PedidoClientDTO.class))).thenReturn(pedidoClientDTO);

        PedidoClientDTO result = usuarioService.realizarPedido(pedidoClientDTO);

        assertThat(result).isNotNull();
        assertThat(result.getIdCliente()).isEqualTo(1L);
        assertThat(result.getEstado()).isEqualTo("Pendiente");
        assertThat(result.getDetallesPedido()).hasSize(1);
        verify(usuarioRepository).existsById(1L);
        verify(pedidoClient).save(any(PedidoClientDTO.class));
    }

    @Test
    void shouldThrowExceptionWhenRealizarPedidoWithNullPedido() {
        assertThatThrownBy(() -> usuarioService.realizarPedido(null))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("no puede ser nulo");
    }

    @Test
    void shouldThrowExceptionWhenRealizarPedidoWithNullIdCliente() {
        pedidoClientDTO.setIdCliente(null);
        assertThatThrownBy(() -> usuarioService.realizarPedido(pedidoClientDTO))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("id del cliente es requerido");
    }

    @Test
    void shouldThrowExceptionWhenRealizarPedidoWithNonExistentCliente() {
        when(usuarioRepository.existsById(999L)).thenReturn(false);
        pedidoClientDTO.setIdCliente(999L);
        
        assertThatThrownBy(() -> usuarioService.realizarPedido(pedidoClientDTO))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("no existe");
        verify(usuarioRepository).existsById(999L);
    }

    @Test
    void shouldThrowExceptionWhenRealizarPedidoWithEmptyDetalles() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        pedidoClientDTO.setDetallesPedido(new ArrayList<>());
        
        assertThatThrownBy(() -> usuarioService.realizarPedido(pedidoClientDTO))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("debe contener al menos un detalle");
        verify(usuarioRepository).existsById(1L);
    }

    @Test
    void shouldThrowExceptionWhenRealizarPedidoWithNullDetalles() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        pedidoClientDTO.setDetallesPedido(null);
        
        assertThatThrownBy(() -> usuarioService.realizarPedido(pedidoClientDTO))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("debe contener al menos un detalle");
        verify(usuarioRepository).existsById(1L);
    }

    @Test
    void shouldPagarPedidoSuccessfully() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(pedidoClient.findByIdPedido(1L)).thenReturn(pedidoClientDTO);
        when(pedidoClient.updateEstadoByIdPedido(1L, "Pagado")).thenReturn("Pedido actualizado");
        when(pedidoClient.findByIdPedido(1L)).thenReturn(pedidoClientDTO);

        PedidoClientDTO result = usuarioService.pagarPedido(1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getIdCliente()).isEqualTo(1L);
        verify(usuarioRepository).existsById(1L);
        verify(pedidoClient, times(2)).findByIdPedido(1L);
        verify(pedidoClient).updateEstadoByIdPedido(1L, "Pagado");
    }

    @Test
    void shouldThrowExceptionWhenPagarPedidoWithNullIds() {
        assertThatThrownBy(() -> usuarioService.pagarPedido(null, 1L))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("son requeridos");
        
        assertThatThrownBy(() -> usuarioService.pagarPedido(1L, null))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("son requeridos");
    }

    @Test
    void shouldThrowExceptionWhenPagarPedidoWithNonExistentCliente() {
        when(usuarioRepository.existsById(999L)).thenReturn(false);
        
        assertThatThrownBy(() -> usuarioService.pagarPedido(999L, 1L))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("no existe");
        verify(usuarioRepository).existsById(999L);
    }

    @Test
    void shouldThrowExceptionWhenPagarPedidoWithNonExistentPedido() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(pedidoClient.findByIdPedido(999L)).thenReturn(null);
        
        assertThatThrownBy(() -> usuarioService.pagarPedido(1L, 999L))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("no existe");
        verify(usuarioRepository).existsById(1L);
        verify(pedidoClient).findByIdPedido(999L);
    }

    @Test
    void shouldThrowExceptionWhenPagarPedidoNotBelongingToCliente() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        pedidoClientDTO.setIdCliente(2L);
        when(pedidoClient.findByIdPedido(1L)).thenReturn(pedidoClientDTO);
        
        assertThatThrownBy(() -> usuarioService.pagarPedido(1L, 1L))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("no pertenece al cliente");
        verify(usuarioRepository).existsById(1L);
        verify(pedidoClient).findByIdPedido(1L);
    }

    @Test
    void shouldThrowExceptionWhenPagarPedidoAlreadyPaid() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        pedidoClientDTO.setEstado("Pagado");
        when(pedidoClient.findByIdPedido(1L)).thenReturn(pedidoClientDTO);
        
        assertThatThrownBy(() -> usuarioService.pagarPedido(1L, 1L))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("ya está pagado");
        verify(usuarioRepository).existsById(1L);
        verify(pedidoClient).findByIdPedido(1L);
    }

    @Test
    void shouldThrowExceptionWhenPagarPedidoCancelled() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        pedidoClientDTO.setEstado("Cancelado");
        when(pedidoClient.findByIdPedido(1L)).thenReturn(pedidoClientDTO);
        
        assertThatThrownBy(() -> usuarioService.pagarPedido(1L, 1L))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("No se puede pagar un pedido cancelado");
        verify(usuarioRepository).existsById(1L);
        verify(pedidoClient).findByIdPedido(1L);
    }

    @Test
    void shouldThrowExceptionWhenPagarPedidoWithEmptyResponse() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(pedidoClient.findByIdPedido(1L)).thenReturn(pedidoClientDTO);
        when(pedidoClient.updateEstadoByIdPedido(1L, "Pagado")).thenReturn("");
        
        assertThatThrownBy(() -> usuarioService.pagarPedido(1L, 1L))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("No se recibió respuesta");
        verify(usuarioRepository).existsById(1L);
        verify(pedidoClient).findByIdPedido(1L);
        verify(pedidoClient).updateEstadoByIdPedido(1L, "Pagado");
    }

    @Test
    void shouldThrowExceptionWhenPagarPedidoWithNullResponse() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(pedidoClient.findByIdPedido(1L)).thenReturn(pedidoClientDTO);
        when(pedidoClient.updateEstadoByIdPedido(1L, "Pagado")).thenReturn(null);
        
        assertThatThrownBy(() -> usuarioService.pagarPedido(1L, 1L))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("No se recibió respuesta");
        verify(usuarioRepository).existsById(1L);
        verify(pedidoClient).findByIdPedido(1L);
        verify(pedidoClient).updateEstadoByIdPedido(1L, "Pagado");
    }

    @Test
    void shouldHandleExceptionWhenPagarPedidoClientFails() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(pedidoClient.findByIdPedido(1L)).thenThrow(new RuntimeException("Error de conexión"));
        
        assertThatThrownBy(() -> usuarioService.pagarPedido(1L, 1L))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("Error al procesar el pago");
        verify(usuarioRepository).existsById(1L);
        verify(pedidoClient).findByIdPedido(1L);
    }

    @Test
    void shouldUpdateAllUsuarioFields() {
        Usuario updated = new Usuario();
        updated.setNombre("María");
        updated.setApellido("González");
        updated.setCorreo("maria.gonzalez@mail.com");
        updated.setTelefono("+56 9 5555 5555");
        updated.setRegion("Biobío");
        updated.setComuna("Concepción");
        updated.setCiudad("Concepción");
        updated.setDireccion("Av. Principal 789");
        updated.setCodigoPostal("1234567");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(updated);

        var result = usuarioService.updateById(1L, updated);
        
        assertThat(result.getContent().getNombre()).isEqualTo("María");
        assertThat(result.getContent().getApellido()).isEqualTo("González");
        assertThat(result.getContent().getCorreo()).isEqualTo("maria.gonzalez@mail.com");
        assertThat(result.getContent().getTelefono()).isEqualTo("+56 9 5555 5555");
        assertThat(result.getContent().getRegion()).isEqualTo("Biobío");
        assertThat(result.getContent().getComuna()).isEqualTo("Concepción");
        assertThat(result.getContent().getCiudad()).isEqualTo("Concepción");
        assertThat(result.getContent().getDireccion()).isEqualTo("Av. Principal 789");
        assertThat(result.getContent().getCodigoPostal()).isEqualTo("1234567");
        
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void shouldValidatePedidoClientDTOStructure() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(pedidoClient.save(any(PedidoClientDTO.class))).thenReturn(pedidoClientDTO);

        PedidoClientDTO result = usuarioService.realizarPedido(pedidoClientDTO);

        assertThat(result).isNotNull();
        assertThat(result.getIdCliente()).isEqualTo(1L);
        assertThat(result.getDetallesPedido()).isNotNull();
        assertThat(result.getDetallesPedido()).hasSize(1);
        assertThat(result.getDetallesPedido().get(0).getIdProducto()).isEqualTo(1L);
        assertThat(result.getDetallesPedido().get(0).getCantidad()).isEqualTo(2);
        assertThat(result.getDetallesPedido().get(0).getPrecio()).isEqualTo(new BigDecimal("15000"));
        
        verify(usuarioRepository).existsById(1L);
        verify(pedidoClient).save(any(PedidoClientDTO.class));
    }

    @Test
    void shouldRealizarPedido() {
        PedidoClientDTO pedidoDTO = new PedidoClientDTO();
        pedidoDTO.setIdCliente(1L);
        pedidoDTO.setMontoFinal(BigDecimal.valueOf(15000));
        pedidoDTO.setEstado("Nuevo");
        pedidoDTO.setDetallesPedido(Arrays.asList(detallePedidoDTO));
        
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(pedidoClient.save(any(PedidoClientDTO.class))).thenReturn(pedidoDTO);
        
        PedidoClientDTO result = usuarioService.realizarPedido(pedidoDTO);
        
        assertThat(result).isNotNull();
        assertThat(result.getIdCliente()).isEqualTo(1L);
        assertThat(result.getMontoFinal()).isEqualTo(BigDecimal.valueOf(15000));
        assertThat(result.getEstado()).isEqualTo("Pendiente");
        
        verify(pedidoClient).save(any(PedidoClientDTO.class));
    }

    @Test
    void shouldPagarPedido() {
        PedidoClientDTO pedidoDTO = new PedidoClientDTO();
        pedidoDTO.setIdCliente(1L);
        pedidoDTO.setIdPedido(100L);
        pedidoDTO.setMontoFinal(BigDecimal.valueOf(15000));
        pedidoDTO.setEstado("Pendiente");
        
        PedidoClientDTO pedidoPagado = new PedidoClientDTO();
        pedidoPagado.setIdCliente(1L);
        pedidoPagado.setIdPedido(100L);
        pedidoPagado.setEstado("Pagado");
        
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(pedidoClient.findByIdPedido(100L)).thenReturn(pedidoDTO).thenReturn(pedidoPagado);
        when(pedidoClient.updateEstadoByIdPedido(100L, "Pagado")).thenReturn("Pedido actualizado");
        
        PedidoClientDTO result = usuarioService.pagarPedido(1L, 100L);
        
        assertThat(result).isNotNull();
        assertThat(result.getIdCliente()).isEqualTo(1L);
        assertThat(result.getIdPedido()).isEqualTo(100L);
        assertThat(result.getEstado()).isEqualTo("Pagado");
        
        verify(usuarioRepository).existsById(1L);
        verify(pedidoClient, times(2)).findByIdPedido(100L);
        verify(pedidoClient).updateEstadoByIdPedido(100L, "Pagado");
    }

    @Test
    void shouldMisPedidos() {
        PedidoClientDTO pedido1 = new PedidoClientDTO();
        pedido1.setIdCliente(1L);
        pedido1.setIdPedido(100L);
        pedido1.setMontoFinal(BigDecimal.valueOf(15000));
        
        PedidoClientDTO pedido2 = new PedidoClientDTO();
        pedido2.setIdCliente(1L);
        pedido2.setIdPedido(101L);
        pedido2.setMontoFinal(BigDecimal.valueOf(20000));
        
        List<PedidoClientDTO> pedidos = Arrays.asList(pedido1, pedido2);
        
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(pedidoClient.findAllByIdCliente(1L)).thenReturn(pedidos);
        
        List<PedidoClientDTO> result = usuarioService.misPedidos(1L);
        
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getIdCliente()).isEqualTo(1L);
        assertThat(result.get(0).getIdPedido()).isEqualTo(100L);
        assertThat(result.get(1).getIdCliente()).isEqualTo(1L);
        assertThat(result.get(1).getIdPedido()).isEqualTo(101L);
        
        verify(usuarioRepository).existsById(1L);
        verify(pedidoClient).findAllByIdCliente(1L);
    }

    @Test
    void shouldThrowExceptionWhenMisPedidosWithNullIdCliente() {
        assertThatThrownBy(() -> usuarioService.misPedidos(null))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("El id del cliente es requerido");
    }

    @Test
    void shouldThrowExceptionWhenMisPedidosWithNonExistentCliente() {
        when(usuarioRepository.existsById(999L)).thenReturn(false);
        
        assertThatThrownBy(() -> usuarioService.misPedidos(999L))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("El cliente con id 999 no existe");
        
        verify(usuarioRepository).existsById(999L);
    }
} 