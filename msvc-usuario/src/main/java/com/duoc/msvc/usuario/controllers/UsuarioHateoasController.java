package com.duoc.msvc.usuario.controllers;

import com.duoc.msvc.usuario.dtos.pojos.PedidoClientDTO;
import com.duoc.msvc.usuario.dtos.UsuarioCreateDTO;
import com.duoc.msvc.usuario.dtos.UsuarioUpdateDTO;
import com.duoc.msvc.usuario.models.entities.Usuario;
import com.duoc.msvc.usuario.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import com.duoc.msvc.usuario.dtos.pojos.PedidoInputDTO;
import com.duoc.msvc.usuario.dtos.pojos.DetallePedidoClientDTO;

@RestController
@RequestMapping("/api/v1/usuarios-hateoas")
@Tag(name = " 2. Usuario (HATEOAS)", description = "Endpoints para gestión de usuarios con HATEOAS. Las respuestas incluyen enlaces para navegación RESTful.")
public class UsuarioHateoasController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista de todos los usuarios registrados con enlaces HATEOAS")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
            content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(this.usuarioService.findAll());
    }

    @Operation(summary = "Obtener usuario por ID", description = "Retorna un usuario dado su ID con enlaces HATEOAS")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> findById(
            @Parameter(description = "ID del usuario", example = "1") @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.usuarioService.findById(id));
    }

    @Operation(summary = "Crear un nuevo usuario", description = "Crea un usuario a partir de los datos enviados en el cuerpo de la petición y retorna con enlaces HATEOAS")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> save(
            @Parameter(description = "Datos del usuario a crear") @Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioCreateDTO.getNombre());
        usuario.setApellido(usuarioCreateDTO.getApellido());
        usuario.setRegion(usuarioCreateDTO.getRegion());
        usuario.setComuna(usuarioCreateDTO.getComuna());
        usuario.setCiudad(usuarioCreateDTO.getCiudad());
        usuario.setCodigoPostal(usuarioCreateDTO.getCodigoPostal());
        usuario.setDireccion(usuarioCreateDTO.getDireccion());
        usuario.setCorreo(usuarioCreateDTO.getCorreo());
        usuario.setContrasena(usuarioCreateDTO.getContrasena());
        usuario.setTelefono(usuarioCreateDTO.getTelefono());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(this.usuarioService.save(usuario));
    }

    @Operation(summary = "Actualizar usuario por ID", description = "Actualiza los datos de un usuario existente y retorna con enlaces HATEOAS")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
            content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> updateById(
            @Parameter(description = "ID del usuario a actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del usuario") @Valid @RequestBody UsuarioUpdateDTO usuarioUpdateDTO){
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioUpdateDTO.getNombre());
        usuario.setApellido(usuarioUpdateDTO.getApellido());
        usuario.setRegion(usuarioUpdateDTO.getRegion());
        usuario.setComuna(usuarioUpdateDTO.getComuna());
        usuario.setCiudad(usuarioUpdateDTO.getCiudad());
        usuario.setCodigoPostal(usuarioUpdateDTO.getCodigoPostal());
        usuario.setDireccion(usuarioUpdateDTO.getDireccion());
        usuario.setCorreo(usuarioUpdateDTO.getCorreo());
        usuario.setContrasena(usuarioUpdateDTO.getContrasena());
        usuario.setTelefono(usuarioUpdateDTO.getTelefono());
        
        return ResponseEntity.status(HttpStatus.OK).body(this.usuarioService.updateById(id, usuario));
    }

    @Operation(summary = "Eliminar usuario por ID", description = "Elimina un usuario existente por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
            content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID del usuario a eliminar", example = "1") @PathVariable Long id){
        this.usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Realizar pedido para un cliente", description = "Permite a un cliente realizar un pedido. Retorna el pedido realizado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido realizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoClientDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
            content = @Content)
    })
    @PostMapping("/{idCliente}/realizarPedido")
    public ResponseEntity<?> realizarPedido(
            @Parameter(description = "ID del cliente", example = "1") @PathVariable Long idCliente,
            @Parameter(description = "Datos del pedido") @Valid @RequestBody PedidoInputDTO pedidoInputDTO) {
        try {
            EntityModel<Usuario> cliente = usuarioService.findById(idCliente);
            PedidoClientDTO pedidoClientDTO = new PedidoClientDTO();
            pedidoClientDTO.setIdCliente(idCliente);
            pedidoClientDTO.setMetodoPago(pedidoInputDTO.getMetodoPago());
            pedidoClientDTO.setDetallesPedido(pedidoInputDTO.getDetallesPedido().stream()
                .map(detalle -> {
                    DetallePedidoClientDTO detalleClient = new DetallePedidoClientDTO();
                    detalleClient.setIdProducto(detalle.getIdProducto());
                    detalleClient.setCantidad(detalle.getCantidad());
                    return detalleClient;
                })
                .collect(Collectors.toList()));
            
            PedidoClientDTO pedidoRealizado = usuarioService.realizarPedido(pedidoClientDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoRealizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cliente con ID " + idCliente + " no encontrado");
        }
    }

    @Operation(summary = "Pagar pedido de un cliente", description = "Permite a un cliente pagar un pedido específico. Retorna el pedido pagado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido pagado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoClientDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente o pedido no encontrado",
            content = @Content)
    })
    @GetMapping("{idCliente}/pagarPedido/{idPedido}")
    public ResponseEntity<PedidoClientDTO> pagarPedido(
            @Parameter(description = "ID del cliente", example = "1") @PathVariable Long idCliente,
            @Parameter(description = "ID del pedido", example = "10") @PathVariable Long idPedido){
        return ResponseEntity.status(HttpStatus.OK).body(this.usuarioService.pagarPedido(idCliente, idPedido));
    }

    @Operation(summary = "Obtener pedidos de un cliente", description = "Retorna la lista de pedidos realizados por un cliente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoClientDTO.class)))
    })
    @GetMapping("{idCliente}/misPedidos")
    public ResponseEntity<List<PedidoClientDTO>> obtenerMisPedidos(
            @Parameter(description = "ID del cliente", example = "1") @PathVariable Long idCliente){
        return ResponseEntity.status(HttpStatus.OK).body(this.usuarioService.misPedidos(idCliente));
    }
} 