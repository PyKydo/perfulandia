package com.duoc.msvc.usuario.services;


import com.duoc.msvc.usuario.assemblers.UsuarioDTOModelAssembler;
import com.duoc.msvc.usuario.clients.PedidoClient;
import com.duoc.msvc.usuario.dtos.UsuarioHateoasDTO;
import com.duoc.msvc.usuario.dtos.pojos.PedidoClientDTO;
import com.duoc.msvc.usuario.exceptions.UsuarioException;
import com.duoc.msvc.usuario.models.entities.Usuario;
import com.duoc.msvc.usuario.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UsuarioServicelmpl implements UsuarioService{

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PedidoClient pedidoClient;
    @Autowired
    private UsuarioDTOModelAssembler assembler;

    @Override
    public CollectionModel<UsuarioHateoasDTO> findAll(){
        List<Usuario> usuarios = this.usuarioRepository.findAll();
        return assembler.toCollectionModel(usuarios);
    }


    @Override
    public UsuarioHateoasDTO findById(Long id){
        Usuario usuario = this.usuarioRepository.findById(id).orElseThrow(
                () -> new UsuarioException("El usuario con id " + id + " no existe")
        );

        return assembler.toModel(usuario);
    }

    @Transactional
    @Override
    public UsuarioHateoasDTO save(Usuario usuario) {
        Usuario savedUsuario = this.usuarioRepository.save(usuario);
        return assembler.toModel(savedUsuario);
    }

    @Transactional
    @Override
    public UsuarioHateoasDTO updateById(Long id, Usuario usuario) {
        Usuario usuarioDb = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioException("El usuario con id " + id + " no existe"));

        usuarioDb.setNombre(usuario.getNombre());
        usuarioDb.setApellido(usuario.getApellido());
        usuarioDb.setCorreo(usuario.getCorreo());
        usuarioDb.setTelefono(usuario.getTelefono());
        usuarioDb.setRegion(usuario.getRegion());
        usuarioDb.setComuna(usuario.getComuna());
        usuarioDb.setCiudad(usuario.getCiudad());
        usuarioDb.setDireccion(usuario.getDireccion());
        usuarioDb.setCodigoPostal(usuario.getCodigoPostal());

        Usuario updatedUsuario = usuarioRepository.save(usuarioDb);
        return assembler.toModel(updatedUsuario);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        usuarioRepository.findById(id).orElseThrow(() -> new UsuarioException("El usuario con id " + id + " no existe"));
        this.usuarioRepository.deleteById(id);
    }

    @Override
    public PedidoClientDTO realizarPedido(PedidoClientDTO pedidoClientDTO) {
        if (pedidoClientDTO == null) {
            throw new UsuarioException("El pedido no puede ser nulo");
        }
        if (pedidoClientDTO.getIdCliente() == null) {
            throw new UsuarioException("El id del cliente es requerido");
        }
        if (!usuarioRepository.existsById(pedidoClientDTO.getIdCliente())) {
            throw new UsuarioException("El cliente con id " + pedidoClientDTO.getIdCliente() + " no existe");
        }
        if (pedidoClientDTO.getDetallesPedido() == null || pedidoClientDTO.getDetallesPedido().isEmpty()) {
            throw new UsuarioException("El pedido debe contener al menos un detalle");
        }

        pedidoClientDTO.setEstado("Pendiente");
        return this.pedidoClient.save(pedidoClientDTO);
    }

    @Override
    public PedidoClientDTO pagarPedido(Long idCliente, Long idPedido) {
        if (idCliente == null || idPedido == null) {
            throw new UsuarioException("El id del cliente y del pedido son requeridos");
        }
        if (!usuarioRepository.existsById(idCliente)) {
            throw new UsuarioException("El cliente con id " + idCliente + " no existe");
        }

        try {
            PedidoClientDTO pedidoClientDTO = pedidoClient.findByIdPedido(idPedido);
            if (pedidoClientDTO == null) {
                throw new UsuarioException("El pedido con id " + idPedido + " no existe");
            }
            if (!pedidoClientDTO.getIdCliente().equals(idCliente)) {
                throw new UsuarioException("El pedido no pertenece al cliente especificado");
            }
            if ("Pagado".equalsIgnoreCase(pedidoClientDTO.getEstado())) {
                throw new UsuarioException("El pedido ya está pagado");
            }
            if ("Cancelado".equalsIgnoreCase(pedidoClientDTO.getEstado())) {
                throw new UsuarioException("No se puede pagar un pedido cancelado");
            }
            
            String resultado = pedidoClient.updateEstadoByIdPedido(idPedido, "Pagado");
            if (resultado == null || resultado.trim().isEmpty()) {
                throw new UsuarioException("No se recibió respuesta del servicio de pedidos");
            }
            
            return pedidoClient.findByIdPedido(idPedido);
        } catch (Exception e) {
            throw new UsuarioException("Error al procesar el pago: " + e.getMessage());
        }
    }

    @Override
    public List<PedidoClientDTO> misPedidos(Long idCliente) {
        if (idCliente == null) {
            throw new UsuarioException("El id del cliente es requerido");
        }
        if (!usuarioRepository.existsById(idCliente)) {
            throw new UsuarioException("El cliente con id " + idCliente + " no existe");
        }
        return pedidoClient.findAllByIdCliente(idCliente);
    }
}
