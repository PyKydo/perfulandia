package com.duoc.msvc.pago.controllers;

import com.duoc.msvc.pago.dtos.PagoGetDTO;
import com.duoc.msvc.pago.dtos.PagoCreateDTO;
import com.duoc.msvc.pago.dtos.PagoUpdateDTO;
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
@RequestMapping("/api/v1/pagos")
@Validated
@Tag(name = "1. Pago (Simple)", description = "Endpoints para gestión de pagos sin HATEOAS. Respuestas simples, ideales para clientes que no requieren enlaces.")
public class PagoController {
    @Autowired
    private PagoService pagoService;
    @Autowired
    private com.duoc.msvc.pago.clients.PedidoClient pedidoClient;

    @Operation(summary = "Obtener todos los pagos (simple)", description = "Retorna una lista simple de pagos sin enlaces HATEOAS")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagoGetDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<PagoGetDTO>> findAll(){
        CollectionModel<EntityModel<Pago>> collectionModel = this.pagoService.findAll();
        List<PagoGetDTO> pagos = collectionModel.getContent().stream()
            .map(entityModel -> convertToGetDTO(entityModel.getContent()))
            .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(pagos);
    }

    @Operation(summary = "Obtener pago por ID (simple)", description = "Retorna un pago dado su ID sin enlaces HATEOAS")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagoGetDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PagoGetDTO> findById(
            @Parameter(description = "ID del pago", example = "1") @PathVariable Long id) {
        EntityModel<Pago> entityModel = this.pagoService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(convertToGetDTO(entityModel.getContent()));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PagoGetDTO>> findByEstado(@PathVariable String estado){
        CollectionModel<EntityModel<Pago>> collectionModel = this.pagoService.findByEstado(estado);
        List<PagoGetDTO> pagos = collectionModel.getContent().stream()
            .map(entityModel -> convertToGetDTO(entityModel.getContent()))
            .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(pagos);
    }

    @Operation(summary = "Crear un nuevo pago (simple)", description = "Crea un pago a partir de los datos enviados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pago creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagoGetDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<PagoGetDTO> save(@Valid @RequestBody PagoCreateDTO pagoCreateDTO){
        Pago pago = convertToEntity(pagoCreateDTO);
        ResponseEntity<com.duoc.msvc.pago.dtos.pojos.PedidoClientDTO> response = this.pedidoClient.findById(pago.getIdPedido());
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new com.duoc.msvc.pago.exceptions.PagoException("El pedido con ID " + pago.getIdPedido() + " no existe");
        }
        EntityModel<Pago> entityModel = pagoService.save(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToGetDTO(entityModel.getContent()));
    }

    @Operation(summary = "Actualizar pago por ID (simple)", description = "Actualiza los datos de un pago existente sin enlaces HATEOAS")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagoGetDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado",
            content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PagoGetDTO> updateById(
            @Parameter(description = "ID del pago a actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del pago") @Valid @RequestBody PagoUpdateDTO pagoUpdateDTO){
        Pago pago = convertToEntity(pagoUpdateDTO);
        EntityModel<Pago> entityModel = this.pagoService.updateById(id, pago);
        return ResponseEntity.status(HttpStatus.OK).body(convertToGetDTO(entityModel.getContent()));
    }

    @Operation(summary = "Eliminar pago por ID (simple)", description = "Elimina un pago existente por su ID")
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

    private PagoGetDTO convertToGetDTO(Pago pago) {
        PagoGetDTO dto = new PagoGetDTO();
        dto.setId(pago.getIdPago());
        dto.setIdPedido(pago.getIdPedido());
        dto.setMonto(pago.getMonto());
        dto.setMetodoPago(pago.getMetodo());
        dto.setEstado(pago.getEstado());
        dto.setFechaPago(pago.getFecha());
        return dto;
    }

    private Pago convertToEntity(PagoCreateDTO dto) {
        Pago pago = new Pago();
        pago.setIdPedido(dto.getIdPedido());
        pago.setMonto(dto.getMonto());
        pago.setMetodo(dto.getMetodoPago());
        return pago;
    }

    private Pago convertToEntity(PagoUpdateDTO dto) {
        Pago pago = new Pago();
        pago.setIdPedido(dto.getIdPedido());
        pago.setMonto(dto.getMonto());
        pago.setMetodo(dto.getMetodoPago());
        pago.setEstado(dto.getEstado());
        return pago;
    }
}
