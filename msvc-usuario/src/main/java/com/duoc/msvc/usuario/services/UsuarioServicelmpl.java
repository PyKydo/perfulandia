package com.duoc.msvc.usuario.services;


import com.duoc.msvc.usuario.clients.PedidoClient;
import com.duoc.msvc.usuario.dtos.pojos.PedidoClientDTO;
import com.duoc.msvc.usuario.exceptions.UsuarioException;
import com.duoc.msvc.usuario.models.entities.Usuario;
import com.duoc.msvc.usuario.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
public class UsuarioServicelmpl implements UsuarioService{

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServicelmpl.class);

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PedidoClient pedidoClient;

    @Override
    public CollectionModel<EntityModel<Usuario>> findAll(){
        logger.info("Iniciando búsqueda de todos los usuarios");
        List<Usuario> usuarios = this.usuarioRepository.findAll();
        List<EntityModel<Usuario>> usuarioModels = usuarios.stream().map(this::toEntityModel).toList();
        logger.info("Búsqueda completada. Se encontraron {} usuarios", usuarios.size());
        return CollectionModel.of(usuarioModels);
    }


    @Override
    public EntityModel<Usuario> findById(Long id){
        logger.info("Buscando usuario con ID: {}", id);
        Usuario usuario = this.usuarioRepository.findById(id).orElseThrow(
                () -> new UsuarioException("El usuario con id " + id + " no existe")
        );
        logger.info("Usuario encontrado: {} {}", usuario.getNombre(), usuario.getApellido());
        return toEntityModel(usuario);
    }

    @Transactional
    @Override
    public EntityModel<Usuario> save(Usuario usuario) {
        logger.info("Iniciando creación de usuario: {} {}", usuario.getNombre(), usuario.getApellido());
        Usuario savedUsuario = this.usuarioRepository.save(usuario);
        logger.info("Usuario creado exitosamente con ID: {}", savedUsuario.getIdUsuario());
        return toEntityModel(savedUsuario);
    }

    @Transactional
    @Override
    public EntityModel<Usuario> updateById(Long id, Usuario usuario) {
        logger.info("Iniciando actualización de usuario con ID: {}", id);
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
        logger.info("Usuario actualizado exitosamente: {} {}", updatedUsuario.getNombre(), updatedUsuario.getApellido());
        return toEntityModel(updatedUsuario);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        logger.info("Iniciando eliminación de usuario con ID: {}", id);
        usuarioRepository.findById(id).orElseThrow(() -> new UsuarioException("El usuario con id " + id + " no existe"));
        this.usuarioRepository.deleteById(id);
        logger.info("Usuario eliminado exitosamente con ID: {}", id);
    }

    @Override
    public PedidoClientDTO realizarPedido(PedidoClientDTO pedidoClientDTO) {
        if (pedidoClientDTO == null) {
            logger.error("Error: El pedido no puede ser nulo");
            throw new UsuarioException("El pedido no puede ser nulo");
        }
        logger.info("Iniciando proceso de realización de pedido para cliente ID: {}", pedidoClientDTO.getIdCliente());
        if (pedidoClientDTO.getIdCliente() == null) {
            logger.error("Error: El id del cliente es requerido");
            throw new UsuarioException("El id del cliente es requerido");
        }
        if (!usuarioRepository.existsById(pedidoClientDTO.getIdCliente())) {
            logger.error("Error: El cliente con id {} no existe", pedidoClientDTO.getIdCliente());
            throw new UsuarioException("El cliente con id " + pedidoClientDTO.getIdCliente() + " no existe");
        }
        if (pedidoClientDTO.getDetallesPedido() == null || pedidoClientDTO.getDetallesPedido().isEmpty()) {
            logger.error("Error: El pedido debe contener al menos un detalle");
            throw new UsuarioException("El pedido debe contener al menos un detalle");
        }

        pedidoClientDTO.setEstado("Pendiente");
        logger.info("Enviando pedido al servicio de pedidos. Estado: {}", pedidoClientDTO.getEstado());
        PedidoClientDTO resultado = this.pedidoClient.save(pedidoClientDTO);
        return resultado;
    }

    @Override
    public PedidoClientDTO pagarPedido(Long idCliente, Long idPedido) {
        logger.info("Iniciando proceso de pago. Cliente ID: {}, Pedido ID: {}", idCliente, idPedido);
        if (idCliente == null || idPedido == null) {
            logger.error("Error: El id del cliente y del pedido son requeridos");
            throw new UsuarioException("El id del cliente y del pedido son requeridos");
        }
        if (!usuarioRepository.existsById(idCliente)) {
            logger.error("Error: El cliente con id {} no existe", idCliente);
            throw new UsuarioException("El cliente con id " + idCliente + " no existe");
        }
        try {
            logger.info("Consultando pedido con ID: {}", idPedido);
            PedidoClientDTO pedidoClientDTO = pedidoClient.findByIdPedido(idPedido);
            if (pedidoClientDTO == null) {
                logger.error("Error: El pedido con id {} no existe", idPedido);
                throw new UsuarioException("El pedido con id " + idPedido + " no existe");
            }
            if (!pedidoClientDTO.getIdCliente().equals(idCliente)) {
                logger.error("Error: El pedido no pertenece al cliente especificado. Pedido cliente: {}, Cliente solicitante: {}", 
                           pedidoClientDTO.getIdCliente(), idCliente);
                throw new UsuarioException("El pedido no pertenece al cliente especificado");
            }
            if ("Pagado".equalsIgnoreCase(pedidoClientDTO.getEstado())) {
                logger.error("Error: El pedido ya está pagado. Estado actual: {}", pedidoClientDTO.getEstado());
                throw new UsuarioException("El pedido ya está pagado");
            }
            if ("Cancelado".equalsIgnoreCase(pedidoClientDTO.getEstado())) {
                logger.error("Error: No se puede pagar un pedido cancelado. Estado actual: {}", pedidoClientDTO.getEstado());
                throw new UsuarioException("No se puede pagar un pedido cancelado");
            }
            logger.info("Actualizando estado del pedido a 'Pagado'");
            String resultado = pedidoClient.updateEstadoByIdPedido(idPedido, "Pagado");
            if (resultado == null || resultado.trim().isEmpty()) {
                logger.error("Error: No se recibió respuesta del servicio de pedidos");
                throw new UsuarioException("No se recibió respuesta del servicio de pedidos");
            }
            logger.info("Pago procesado exitosamente. Obteniendo pedido actualizado");
            return pedidoClient.findByIdPedido(idPedido);
        } catch (Exception e) {
            logger.error("Error al procesar el pago: {}", e.getMessage(), e);
            throw new UsuarioException("Error al procesar el pago: " + e.getMessage());
        }
    }

    @Override
    public List<PedidoClientDTO> misPedidos(Long idCliente) {
        logger.info("Consultando pedidos del cliente ID: {}", idCliente);
        
        if (idCliente == null) {
            logger.error("Error: El id del cliente es requerido");
            throw new UsuarioException("El id del cliente es requerido");
        }
        if (!usuarioRepository.existsById(idCliente)) {
            logger.error("Error: El cliente con id {} no existe", idCliente);
            throw new UsuarioException("El cliente con id " + idCliente + " no existe");
        }
        
        List<PedidoClientDTO> pedidos = pedidoClient.findAllByIdCliente(idCliente);
        logger.info("Se encontraron {} pedidos para el cliente ID: {}", pedidos.size(), idCliente);
        return pedidos;
    }

    private EntityModel<Usuario> toEntityModel(Usuario usuario) {
        return EntityModel.of(usuario,
            linkTo(methodOn(com.duoc.msvc.usuario.controllers.UsuarioHateoasController.class).findById(usuario.getIdUsuario())).withSelfRel(),
            linkTo(methodOn(com.duoc.msvc.usuario.controllers.UsuarioHateoasController.class).findAll()).withRel("usuarios"),
            linkTo(methodOn(com.duoc.msvc.usuario.controllers.UsuarioHateoasController.class).obtenerMisPedidos(usuario.getIdUsuario())).withRel("misPedidos"),
            linkTo(methodOn(com.duoc.msvc.usuario.controllers.UsuarioHateoasController.class).realizarPedido(usuario.getIdUsuario(), null)).withRel("realizarPedido")
        );
    }
}
