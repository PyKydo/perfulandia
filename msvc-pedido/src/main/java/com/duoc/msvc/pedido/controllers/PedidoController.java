package com.duoc.msvc.pedido.controllers;

import com.duoc.msvc.pedido.dtos.PedidoGetDTO;
import com.duoc.msvc.pedido.dtos.PedidoCreateDTO;
import com.duoc.msvc.pedido.dtos.PedidoUpdateDTO;
import com.duoc.msvc.pedido.dtos.DetallePedidoDTO;
import com.duoc.msvc.pedido.dtos.pojos.PedidoClientDTO;
import com.duoc.msvc.pedido.models.entities.Pedido;
import com.duoc.msvc.pedido.services.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pedidos")
@Validated
@Tag(name = "1. Pedido (Simple)", description = "Endpoints para gestión de pedidos sin HATEOAS. Respuestas simples, ideales para clientes que no requieren enlaces.")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @Operation(summary = "Obtener todos los pedidos (simple)", description = "Retorna una lista simple de pedidos sin enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoGetDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<PedidoGetDTO>> findAll(){
        CollectionModel<EntityModel<Pedido>> collectionModel = this.pedidoService.findAll();
        List<PedidoGetDTO> pedidos = collectionModel.getContent().stream()
            .map(entityModel -> convertToGetDTO(entityModel.getContent()))
            .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(pedidos);
    }

    @Operation(summary = "Obtener pedido por ID (simple)", description = "Retorna un pedido por su ID sin enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoGetDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoGetDTO> findById(
            @Parameter(description = "ID único del pedido", example = "1") @PathVariable Long id) {
        EntityModel<Pedido> entityModel = this.pedidoService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(convertToGetDTO(entityModel.getContent()));
    }

    @GetMapping("/cliente/{id}")
    @Operation(summary = "Obtener pedidos por usuario (simple)", description = "Retorna pedidos de un usuario específico sin enlaces HATEOAS.")
    public ResponseEntity<List<PedidoGetDTO>> findAllByIdCliente(
            @Parameter(description = "ID único del usuario", example = "1") @PathVariable Long id){
        CollectionModel<EntityModel<Pedido>> collectionModel = this.pedidoService.findByIdCliente(id);
        List<PedidoGetDTO> pedidos = collectionModel.getContent().stream()
            .map(entityModel -> convertToGetDTO(entityModel.getContent()))
            .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(pedidos);
    }

    @GetMapping("/cliente/{idCliente}/client")
    @Operation(summary = "Obtener pedidos por usuario (para microservicios)", description = "Retorna pedidos de un usuario específico como PedidoClientDTO para comunicación entre microservicios.")
    public ResponseEntity<List<PedidoClientDTO>> findAllByIdClienteForClient(
            @Parameter(description = "ID único del usuario", example = "1") @PathVariable Long idCliente){
        CollectionModel<EntityModel<Pedido>> collectionModel = this.pedidoService.findByIdCliente(idCliente);
        List<PedidoClientDTO> pedidos = collectionModel.getContent().stream()
            .map(entityModel -> this.pedidoService.convertToClientDTO(entityModel.getContent()))
            .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(pedidos);
    }

    @GetMapping("/{idPedido}/client")
    @Operation(summary = "Obtener pedido por ID (para microservicios)", description = "Retorna un pedido por su ID como PedidoClientDTO para comunicación entre microservicios.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoClientDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado",
            content = @Content)
    })
    public ResponseEntity<PedidoClientDTO> findByIdForClient(
            @Parameter(description = "ID único del pedido", example = "1") @PathVariable Long idPedido) {
        EntityModel<Pedido> entityModel = this.pedidoService.findById(idPedido);
        PedidoClientDTO pedidoClientDTO = this.pedidoService.convertToClientDTO(entityModel.getContent());
        return ResponseEntity.status(HttpStatus.OK).body(pedidoClientDTO);
    }

    @Operation(summary = "Crear un nuevo pedido (simple)", description = "Crea un pedido a partir de los datos enviados en el cuerpo de la petición.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoGetDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<PedidoGetDTO> save(
            @Parameter(description = "Datos del pedido a crear") 
            @Valid @RequestBody PedidoCreateDTO pedidoInput) {
        Pedido pedido = new Pedido();
        pedido.setIdCliente(pedidoInput.getIdCliente());
        pedido.setMetodoPago(pedidoInput.getMetodoPago());
        if (pedidoInput.getDetallesPedido() != null) {
            var detalles = pedidoInput.getDetallesPedido().stream()
                .map(detalleInput -> {
                    var detalle = new com.duoc.msvc.pedido.models.entities.DetallePedido();
                    detalle.setIdProducto(detalleInput.getIdProducto());
                    detalle.setCantidad(detalleInput.getCantidad());
                    detalle.setPedido(pedido);
                    return detalle;
                })
                .toList();
            pedido.setDetallesPedido(detalles);
        }
        
        EntityModel<Pedido> entityModel = this.pedidoService.save(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToGetDTO(entityModel.getContent()));
    }

    @Operation(summary = "Crear un nuevo pedido (para microservicios)", description = "Crea un pedido y devuelve PedidoClientDTO para comunicación entre microservicios.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoClientDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    @PostMapping("/cliente")
    public ResponseEntity<PedidoClientDTO> saveForClient(
            @Parameter(description = "Datos del pedido a crear") 
            @Valid @RequestBody PedidoClientDTO pedidoClientDTO) {
        Pedido pedido = new Pedido();
        pedido.setIdCliente(pedidoClientDTO.getIdCliente());
        pedido.setMetodoPago(pedidoClientDTO.getMetodoPago());
        pedido.setEstado(pedidoClientDTO.getEstado());
        if (pedidoClientDTO.getDetallesPedido() != null) {
            var detalles = pedidoClientDTO.getDetallesPedido().stream()
                .map(detalleClient -> {
                    var detalle = new com.duoc.msvc.pedido.models.entities.DetallePedido();
                    detalle.setIdProducto(detalleClient.getIdProducto());
                    detalle.setCantidad(detalleClient.getCantidad());
                    detalle.setPedido(pedido);
                    return detalle;
                })
                .toList();
            pedido.setDetallesPedido(detalles);
        }
        
        EntityModel<Pedido> entityModel = this.pedidoService.save(pedido);
        PedidoClientDTO resultado = this.pedidoService.convertToClientDTO(entityModel.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @Operation(summary = "Actualizar pedido por ID (simple)", description = "Actualiza los datos de un pedido existente sin enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoGetDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado",
            content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PedidoGetDTO> updateById(
            @Parameter(description = "ID único del pedido a actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del pedido") 
            @Valid @RequestBody PedidoUpdateDTO pedidoUpdate) {
        Pedido pedido = new Pedido();
        pedido.setMetodoPago(pedidoUpdate.getMetodoPago());
        pedido.setEstado(pedidoUpdate.getEstado());
        
        EntityModel<Pedido> entityModel = this.pedidoService.updateById(id, pedido);
        return ResponseEntity.status(HttpStatus.OK).body(convertToGetDTO(entityModel.getContent()));
    }

    @PutMapping("/{id}/actualizarEstado/{nuevoEstado}")
    @Operation(summary = "Actualizar estado del pedido (simple)", description = "Actualiza el estado de un pedido existente sin enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado",
            content = @Content)
    })
    public ResponseEntity<String> updateEstadoById(
            @Parameter(description = "ID único del pedido", example = "1") @PathVariable Long id, 
            @Parameter(description = "Nuevo estado", example = "Pagado") @PathVariable String nuevoEstado){
        return ResponseEntity.status(HttpStatus.OK).body(this.pedidoService.updateEstadoById(id, nuevoEstado));
    }

    @Operation(summary = "Eliminar pedido por ID (simple)", description = "Elimina un pedido existente por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pedido eliminado exitosamente",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado",
            content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID único del pedido a eliminar", example = "1") @PathVariable Long id){
        this.pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private PedidoGetDTO convertToGetDTO(Pedido pedido) {
        return this.pedidoService.convertToDTO(pedido);
    }
}
