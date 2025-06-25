package com.duoc.msvc.usuario.services;

import com.duoc.msvc.usuario.dtos.UsuarioHateoasDTO;
import com.duoc.msvc.usuario.dtos.pojos.PedidoClientDTO;
import com.duoc.msvc.usuario.models.entities.Usuario;
import org.springframework.hateoas.CollectionModel;

import java.util.List;

public interface UsuarioService {
    CollectionModel<UsuarioHateoasDTO> findAll();
    UsuarioHateoasDTO findById(Long id);
    UsuarioHateoasDTO save(Usuario usuario);
    UsuarioHateoasDTO updateById(Long id, Usuario usuarioActualizado);
    void deleteById(Long id);
    PedidoClientDTO realizarPedido(PedidoClientDTO pedidoClientDTO);
    PedidoClientDTO pagarPedido(Long idCliente, Long idPedido);
    List<PedidoClientDTO> misPedidos(Long idCliente);
}
