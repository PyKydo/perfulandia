package com.duoc.msvc.pago.controllers;

import com.duoc.msvc.pago.clients.PedidoClient;
import com.duoc.msvc.pago.dtos.PagoDTO;
import com.duoc.msvc.pago.dtos.pojos.PedidoClientDTO;
import com.duoc.msvc.pago.exceptions.PagoException;
import com.duoc.msvc.pago.models.Pago;
import com.duoc.msvc.pago.services.PagoService;
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
@RequestMapping("/api/v1/pagos")
@Validated
public class PagoController {
    @Autowired
    private PagoService pagoService;
    @Autowired
    private PedidoClient pedidoClient;

    @Operation(summary = "Obtener todos los pagos", description = "Retorna una lista de todos los pagos registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagoDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<PagoDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(this.pagoService.findAll());
    }

    @Operation(summary = "Obtener pago por ID", description = "Retorna un pago dado su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> findById(
            @Parameter(description = "ID del pago", example = "1") @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.pagoService.findById(id));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PagoDTO>> findByEstado(@PathVariable String estado){
        return ResponseEntity.status(HttpStatus.OK).body(this.pagoService.findByEstado(estado));
    }

    @Operation(summary = "Crear un nuevo pago", description = "Crea un pago a partir de los datos enviados en el cuerpo de la petición")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pago creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagoDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<PagoDTO> save(@Valid @RequestBody Pago pago){
        ResponseEntity<PedidoClientDTO> response = this.pedidoClient.findById(pago.getIdPedido());

        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new PagoException("El pedido con ID " + pago.getIdPedido() + " no existe");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.save(pago));
    }

    @Operation(summary = "Actualizar pago por ID", description = "Actualiza los datos de un pago existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado",
            content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PagoDTO> updateById(
            @Parameter(description = "ID del pago a actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del pago") @RequestBody Pago pago){
        return ResponseEntity.status(HttpStatus.OK).body(this.pagoService.updateById(id, pago));
    }

    @Operation(summary = "Eliminar pago por ID", description = "Elimina un pago existente por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pago eliminado exitosamente",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado",
            content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID del pago a eliminar", example = "1") @PathVariable Long id){
        this.pagoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/actualizarEstado/{idPedido}/{nuevoEstado}")
    public ResponseEntity<String> updateEstadoByIdPedido(@PathVariable Long idPedido, @PathVariable String nuevoEstado){
        return ResponseEntity.status(HttpStatus.OK).body(pagoService.updateEstadoById(idPedido, nuevoEstado));
    }
}
