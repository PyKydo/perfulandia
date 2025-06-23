package com.duoc.msvc.usuario.services;

import com.duoc.msvc.usuario.clients.PedidoClient;
import com.duoc.msvc.usuario.dtos.UsuarioDTO;
import com.duoc.msvc.usuario.exceptions.UsuarioException;
import com.duoc.msvc.usuario.models.entities.Usuario;
import com.duoc.msvc.usuario.repositories.UsuarioRepository;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PedidoClient pedidoClient;
    @InjectMocks
    private UsuarioServicelmpl usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void shouldListAllUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));
        List<UsuarioDTO> result = usuarioService.findAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Juan");
        verify(usuarioRepository).findAll();
    }

    @Test
    void shouldFindUsuarioById() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        UsuarioDTO dto = usuarioService.findById(1L);
        assertThat(dto.getNombre()).isEqualTo("Juan");
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
        UsuarioDTO dto = usuarioService.save(usuario);
        assertThat(dto.getNombre()).isEqualTo("Juan");
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

        UsuarioDTO dto = usuarioService.updateById(1L, updated);
        assertThat(dto.getNombre()).isEqualTo("Pedro");
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
    void shouldConvertToDTO() {
        UsuarioDTO dto = usuarioService.convertToDTO(usuario);
        assertThat(dto.getNombre()).isEqualTo("Juan");
        assertThat(dto.getApellido()).isEqualTo("Pérez");
        assertThat(dto.getCorreo()).isEqualTo("juan.perez@mail.com");
    }
} 