package com.duoc.msvc.pedido.controllers;

import com.duoc.msvc.pedido.dtos.PedidoHateoasDTO;
import com.duoc.msvc.pedido.dtos.PedidoCreateDTO;
import com.duoc.msvc.pedido.dtos.PedidoUpdateDTO;
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
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pedidos-hateoas")
@Validated
@Tag(name = "Pedido (HATEOAS)", description = "Endpoints para gestión de pedidos con HATEOAS. Las respuestas incluyen enlaces para navegación RESTful.")
public class PedidoHATEOASController {
    
    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    @Operation(summary = "Obtener todos los pedidos (HATEOAS)", description = "Retorna una lista de pedidos con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoHateoasDTO.class)))
    })
    public ResponseEntity<CollectionModel<PedidoHateoasDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(this.pedidoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID (HATEOAS)", description = "Retorna un pedido por su ID con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoHateoasDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado",
            content = @Content)
    })
    public ResponseEntity<PedidoHateoasDTO> findById(
            @Parameter(description = "ID único del pedido", example = "1") @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.pedidoService.findById(id));
    }

    @GetMapping("/cliente/{idCliente}")
    @Operation(summary = "Obtener pedidos por usuario (HATEOAS)", description = "Retorna pedidos de un usuario específico con enlaces HATEOAS.")
    public ResponseEntity<CollectionModel<PedidoHateoasDTO>> findByIdCliente(
            @Parameter(description = "ID único del usuario", example = "1") @PathVariable Long idCliente){
        return ResponseEntity.status(HttpStatus.OK).body(this.pedidoService.findByIdCliente(idCliente));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo pedido (HATEOAS)", description = "Crea un pedido a partir de los datos enviados en el cuerpo de la petición.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoHateoasDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    public ResponseEntity<PedidoHateoasDTO> save(
            @Parameter(description = "Datos del pedido a crear") 
            @Valid @RequestBody PedidoCreateDTO pedidoInput) {
        // Convertir PedidoInputDTO a Pedido
        Pedido pedido = new Pedido();
        pedido.setIdCliente(pedidoInput.getIdCliente());
        pedido.setMetodoPago(pedidoInput.getMetodoPago());
        
        // Convertir detalles
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
        
        return ResponseEntity.status(HttpStatus.CREATED).body(this.pedidoService.save(pedido));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pedido por ID (HATEOAS)", description = "Actualiza los datos de un pedido existente con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoHateoasDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado",
            content = @Content)
    })
    public ResponseEntity<PedidoHateoasDTO> updateById(
            @Parameter(description = "ID único del pedido a actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del pedido") 
            @Valid @RequestBody PedidoUpdateDTO pedidoUpdate){
        // Convertir PedidoUpdateDTO a Pedido
        Pedido pedido = new Pedido();
        pedido.setMetodoPago(pedidoUpdate.getMetodoPago());
        pedido.setEstado(pedidoUpdate.getEstado());
        
        return ResponseEntity.status(HttpStatus.OK).body(this.pedidoService.updateById(id, pedido));
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del pedido (HATEOAS)", description = "Actualiza el estado de un pedido existente con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado",
            content = @Content)
    })
    public ResponseEntity<String> updateEstadoById(
            @Parameter(description = "ID único del pedido", example = "1") @PathVariable Long id,
            @Parameter(description = "Nuevo estado", example = "Pagado") @RequestParam String nuevoEstado){
        return ResponseEntity.status(HttpStatus.OK).body(this.pedidoService.updateEstadoById(id, nuevoEstado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pedido por ID (HATEOAS)", description = "Elimina un pedido existente por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pedido eliminado exitosamente",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado",
            content = @Content)
    })
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID único del pedido a eliminar", example = "1") @PathVariable Long id){
        this.pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 