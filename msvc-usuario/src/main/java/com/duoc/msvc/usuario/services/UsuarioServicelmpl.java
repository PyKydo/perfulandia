package com.duoc.msvc.usuario.services;


import com.duoc.msvc.usuario.clients.PedidoClient;
import com.duoc.msvc.usuario.dtos.UsuarioDTO;
import com.duoc.msvc.usuario.dtos.pojos.PedidoClientDTO;
import com.duoc.msvc.usuario.exceptions.UsuarioException;
import com.duoc.msvc.usuario.models.entities.Usuario;
import com.duoc.msvc.usuario.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UsuarioServicelmpl implements UsuarioService{

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PedidoClient pedidoClient;

    @Override
    public List<UsuarioDTO> findAll(){
        return this.usuarioRepository.findAll().stream().map(this::convertToDTO).toList();
    }


    @Override
    public UsuarioDTO findById(Long id){
        Usuario usuario = this.usuarioRepository.findById(id).orElseThrow(
                () -> new UsuarioException("El usuario con id " + id + " no existe")
        );

        return convertToDTO(usuario);
    }

    @Override
    public UsuarioDTO save(Usuario usuario) {
        return convertToDTO(this.usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioDTO updateById(Long id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioException("El usuario con id " + id + " no existe"));

        usuarioActualizado.setIdUsuario(usuarioExistente.getIdUsuario());

        return convertToDTO(usuarioRepository.save(usuarioActualizado));
    }

    @Override
    public void deleteById(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioException("El usuario con id " + id + " no existe");
        }
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

    @Override
    public UsuarioDTO convertToDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdCliente(usuario.getIdUsuario());
        dto.setCiudad(usuario.getCiudad());
        dto.setDireccion(usuario.getDireccion());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setCorreo(usuario.getCorreo());
        dto.setTelefono(usuario.getTelefono());
        dto.setComuna(usuario.getComuna());
        dto.setRegion(usuario.getRegion());
        dto.setCodigoPostal(usuario.getCodigoPostal());
        return dto;
    }
}
