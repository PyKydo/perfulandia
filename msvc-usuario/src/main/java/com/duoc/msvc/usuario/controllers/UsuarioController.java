package com.duoc.msvc.usuario.controllers;

import com.duoc.msvc.usuario.dtos.UsuarioSimpleDTO;
import com.duoc.msvc.usuario.dtos.pojos.PedidoClientDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/usuarios-simple")
@Tag(name = "Usuario (Simple)", description = "API REST para la gestión de usuarios en Perfulandia. Versión simple.")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Obtiene todos los usuarios", description = "Obtiene una lista simple de todos los usuarios registrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioSimpleDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<UsuarioSimpleDTO>> findAll(){
        var collectionModel = this.usuarioService.findAll();
        List<UsuarioSimpleDTO> usuarios = collectionModel.getContent().stream()
            .map(this::convertToSimpleDTO)
            .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }

    @Operation(summary = "Obtiene un usuario por su ID", description = "Obtiene un usuario específico por su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioSimpleDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioSimpleDTO> findById(
            @Parameter(description = "ID único del usuario", example = "1") @PathVariable Long id) {
        var usuarioHateoas = this.usuarioService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(convertToSimpleDTO(usuarioHateoas));
    }

    @Operation(summary = "Crea un nuevo usuario", description = "Crea un nuevo usuario a partir de los datos enviados en el cuerpo de la petición.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioSimpleDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<UsuarioSimpleDTO> save(
            @Parameter(description = "Usuario a crear") @Valid @RequestBody Usuario usuario) {
        var usuarioHateoas = this.usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToSimpleDTO(usuarioHateoas));
    }

    @Operation(summary = "Actualiza un usuario existente", description = "Actualiza los datos de un usuario existente por su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioSimpleDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
            content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioSimpleDTO> updateById(
            @Parameter(description = "ID único del usuario a actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del usuario") @RequestBody Usuario usuario){
        var usuarioHateoas = this.usuarioService.updateById(id, usuario);
        return ResponseEntity.status(HttpStatus.OK).body(convertToSimpleDTO(usuarioHateoas));
    }

    @Operation(summary = "Elimina un usuario por su ID", description = "Elimina un usuario existente por su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
            content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID único del usuario a eliminar", example = "1") @PathVariable Long id){
        this.usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Realiza un pedido para un usuario", description = "Permite a un usuario realizar un pedido. Retorna el pedido realizado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido realizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoClientDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
            content = @Content)
    })
    @PostMapping("/{idCliente}/realizarPedido")
    public ResponseEntity<?> realizarPedido(
            @Parameter(description = "ID único del usuario", example = "1") @PathVariable Long idCliente,
            @Parameter(description = "Datos del pedido a realizar") @RequestBody PedidoClientDTO pedidoClientDTO) {
        try {
            UsuarioSimpleDTO cliente = convertToSimpleDTO(usuarioService.findById(idCliente));
            pedidoClientDTO.setIdCliente(idCliente);
            PedidoClientDTO pedidoRealizado = usuarioService.realizarPedido(pedidoClientDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoRealizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cliente con ID " + idCliente + " no encontrado");
        }
    }

    @Operation(summary = "Paga un pedido de un usuario", description = "Permite a un usuario pagar un pedido específico. Retorna el pedido pagado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido pagado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoClientDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente o pedido no encontrado",
            content = @Content)
    })
    @GetMapping("{idCliente}/pagarPedido/{idPedido}")
    public ResponseEntity<PedidoClientDTO> pagarPedido(
            @Parameter(description = "ID único del usuario", example = "1") @PathVariable Long idCliente,
            @Parameter(description = "ID único del pedido", example = "10") @PathVariable Long idPedido){
        return ResponseEntity.status(HttpStatus.OK).body(this.usuarioService.pagarPedido(idCliente, idPedido));
    }

    @Operation(summary = "Obtiene los pedidos de un usuario", description = "Obtiene la lista de pedidos realizados por un usuario.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoClientDTO.class)))
    })
    @GetMapping("{idCliente}/misPedidos")
    public ResponseEntity<List<PedidoClientDTO>> obtenerMisPedidos(
            @Parameter(description = "ID único del usuario", example = "1") @PathVariable Long idCliente){
        return ResponseEntity.status(HttpStatus.OK).body(this.usuarioService.misPedidos(idCliente));
    }

    private UsuarioSimpleDTO convertToSimpleDTO(com.duoc.msvc.usuario.dtos.UsuarioHateoasDTO usuarioHateoasDTO) {
        UsuarioSimpleDTO dto = new UsuarioSimpleDTO();
        dto.setId(usuarioHateoasDTO.getId());
        dto.setNombre(usuarioHateoasDTO.getNombre());
        dto.setApellido(usuarioHateoasDTO.getApellido());
        dto.setRegion(usuarioHateoasDTO.getRegion());
        dto.setComuna(usuarioHateoasDTO.getComuna());
        dto.setCiudad(usuarioHateoasDTO.getCiudad());
        dto.setCodigoPostal(usuarioHateoasDTO.getCodigoPostal());
        dto.setDireccion(usuarioHateoasDTO.getDireccion());
        dto.setCorreo(usuarioHateoasDTO.getCorreo());
        dto.setContrasena(usuarioHateoasDTO.getContrasena());
        dto.setTelefono(usuarioHateoasDTO.getTelefono());
        return dto;
    }
}
