package com.duoc.msvc.usuario.services;

import com.duoc.msvc.usuario.dtos.UsuarioDTO;
import com.duoc.msvc.usuario.dtos.pojos.PedidoClientDTO;
import com.duoc.msvc.usuario.models.entities.Usuario;

import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO> findAll();
    UsuarioDTO findById(Long id);
    UsuarioDTO save(Usuario usuario);
    UsuarioDTO updateById(Long id, Usuario usuario);
    void deleteById(Long id);
    PedidoClientDTO realizarPedido(PedidoClientDTO pedidoClientDTO);
    PedidoClientDTO pagarPedido(Long idCliente, Long idPedido);
    List<PedidoClientDTO> misPedidos(Long idCliente);
    UsuarioDTO convertToDTO(Usuario usuario);
}
