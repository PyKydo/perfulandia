package com.duoc.msvc.envio.controllers;

import com.duoc.msvc.envio.dtos.EnvioCreateDTO;
import com.duoc.msvc.envio.dtos.EnvioUpdateDTO;
import com.duoc.msvc.envio.models.entities.Envio;
import com.duoc.msvc.envio.services.EnvioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/envios-hateoas")
@Validated
@Tag(name = " 2. Envío (HATEOAS)", description = "Endpoints para gestión de envíos con HATEOAS. Las respuestas incluyen enlaces para navegación RESTful.")
public class EnvioHateoasController {
    @Autowired
    private EnvioService envioService;

    @GetMapping
    @Operation(summary = "Obtener todos los envíos (HATEOAS)", description = "Retorna una lista de envíos con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de envíos obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Envio.class)))
    })
    public ResponseEntity<CollectionModel<EntityModel<Envio>>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(envioService.findAllHateoas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener envío por ID (HATEOAS)", description = "Retorna un envío por su ID con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Envío encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Envio.class))),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado",
            content = @Content)
    })
    public ResponseEntity<EntityModel<Envio>> findById(
            @Parameter(description = "ID del envío", example = "1") @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(envioService.findByIdHateoas(id));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo envío (HATEOAS)", description = "Crea un envío y retorna la entidad con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Envío creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Envio.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    public ResponseEntity<EntityModel<Envio>> save(@RequestBody @Valid EnvioCreateDTO envioCreateDTO) {
        Envio envio = new Envio();
        envio.setIdPedido(envioCreateDTO.getIdPedido());
        envio.setDireccion(envioCreateDTO.getDireccion());
        envio.setRegion(envioCreateDTO.getRegion());
        envio.setCiudad(envioCreateDTO.getCiudad());
        envio.setComuna(envioCreateDTO.getComuna());
        envio.setCodigoPostal(envioCreateDTO.getCodigoPostal());
        envio.setCosto(envioCreateDTO.getCosto());
        var saved = envioService.save(envio);
        return ResponseEntity.status(HttpStatus.CREATED).body(envioService.findByIdHateoas(saved.getId()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar envío por ID (HATEOAS)", description = "Actualiza un envío existente y retorna la entidad con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Envío actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Envio.class))),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado",
            content = @Content)
    })
    public ResponseEntity<EntityModel<Envio>> updateById(
            @Parameter(description = "ID del envío a actualizar", example = "1") @PathVariable Long id,
            @RequestBody @Valid EnvioUpdateDTO envioUpdateDTO) {
        Envio envio = new Envio();
        envio.setDireccion(envioUpdateDTO.getDireccion());
        envio.setRegion(envioUpdateDTO.getRegion());
        envio.setCiudad(envioUpdateDTO.getCiudad());
        envio.setComuna(envioUpdateDTO.getComuna());
        envio.setCodigoPostal(envioUpdateDTO.getCodigoPostal());
        envio.setEstado(envioUpdateDTO.getEstado());
        envio.setFechaEstimadaEntrega(envioUpdateDTO.getFechaEstimadaEntrega());
        var updated = envioService.updateById(id, envio);
        return ResponseEntity.status(HttpStatus.OK).body(envioService.findByIdHateoas(updated.getId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar envío por ID (HATEOAS)", description = "Elimina un envío existente por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Envío eliminado exitosamente",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado",
            content = @Content)
    })
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID del envío a eliminar", example = "1") @PathVariable Long id){
        envioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/costoEnvio")
    @Operation(summary = "Obtener costo de envío (HATEOAS)", description = "Retorna el costo estimado de un envío.")
    public ResponseEntity<BigDecimal> getCostoEnvio(){
        return ResponseEntity.status(HttpStatus.OK).body(envioService.getCostoEnvio());
    }

    @PutMapping("/actualizarEstado/{idPedido}/{nuevoEstado}")
    @Operation(summary = "Actualizar estado de envío por ID de pedido (HATEOAS)", description = "Actualiza el estado de un envío asociado a un pedido.")
    public ResponseEntity<String> updateEstadoByIdPedido(@PathVariable Long idPedido, @PathVariable String nuevoEstado){
        return ResponseEntity.status(HttpStatus.OK).body(envioService.updateEstadoById(idPedido, nuevoEstado));
    }
} 