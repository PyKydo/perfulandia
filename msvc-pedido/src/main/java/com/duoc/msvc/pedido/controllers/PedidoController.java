package com.duoc.msvc.pedido.controllers;

import com.duoc.msvc.pedido.dtos.PedidoDTO;
import com.duoc.msvc.pedido.models.entities.Pedido;
import com.duoc.msvc.pedido.services.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
@Validated
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @Operation(summary = "Obtener todos los pedidos", description = "Retorna una lista de todos los pedidos registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(this.pedidoService.findAll());
    }

    @Operation(summary = "Obtener pedido por ID", description = "Retorna un pedido dado su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> findById(
            @Parameter(description = "ID del pedido", example = "1") @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.pedidoService.findById(id));
    }

    @Operation(summary = "Crear un nuevo pedido", description = "Crea un pedido a partir de los datos enviados en el cuerpo de la petición")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<PedidoDTO> save(
            @Parameter(description = "Pedido a crear") @RequestBody Pedido pedido) {
        if (pedido.getDetallesPedido() != null) {
            pedido.getDetallesPedido().forEach(detalle -> detalle.setPedido(pedido));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.pedidoService.save(pedido));
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<PedidoDTO>> findAllByIdCliente(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(this.pedidoService.findByIdCliente(id));
    }

    @Operation(summary = "Actualizar pedido por ID", description = "Actualiza los datos de un pedido existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado",
            content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> updateById(
            @Parameter(description = "ID del pedido a actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del pedido") @RequestBody Pedido pedido){
        return ResponseEntity.status(HttpStatus.OK).body(this.pedidoService.updateById(id, pedido));
    }

    @Operation(summary = "Eliminar pedido por ID", description = "Elimina un pedido existente por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pedido eliminado exitosamente",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado",
            content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID del pedido a eliminar", example = "1") @PathVariable Long id){
        this.pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/actualizarEstado/{nuevoEstado}")
    public ResponseEntity<String> updateEstadoById(@PathVariable Long id, @PathVariable String nuevoEstado){
        return ResponseEntity.status(HttpStatus.OK).body(this.pedidoService.updateEstadoById(id, nuevoEstado));
    }
}
