package com.duoc.msvc.usuario.services;

import com.duoc.msvc.usuario.dtos.pojos.PedidoClientDTO;
import com.duoc.msvc.usuario.models.entities.Usuario;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;

public interface UsuarioService {
    CollectionModel<EntityModel<Usuario>> findAll();
    EntityModel<Usuario> findById(Long id);
    EntityModel<Usuario> save(Usuario usuario);
    EntityModel<Usuario> updateById(Long id, Usuario usuarioActualizado);
    void deleteById(Long id);
    PedidoClientDTO realizarPedido(PedidoClientDTO pedidoClientDTO);
    PedidoClientDTO pagarPedido(Long idCliente, Long idPedido);
    List<PedidoClientDTO> misPedidos(Long idCliente);
}
