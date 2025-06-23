package com.duoc.msvc.envio.controllers;

import com.duoc.msvc.envio.dtos.EnvioDTO;
import com.duoc.msvc.envio.models.entities.Envio;
import com.duoc.msvc.envio.services.EnvioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/envios")
@Validated
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @Operation(summary = "Obtener todos los envíos", description = "Retorna una lista de todos los envíos registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de envíos obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnvioDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<EnvioDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(envioService.findAll());
    }

    @Operation(summary = "Obtener envío por ID", description = "Retorna un envío dado su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Envío encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnvioDTO.class))),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EnvioDTO> findById(
            @Parameter(description = "ID del envío", example = "1") @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(envioService.findById(id));
    }

    @Operation(summary = "Crear un nuevo envío", description = "Crea un envío a partir de los datos enviados en el cuerpo de la petición")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Envío creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnvioDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<EnvioDTO> save(
            @Parameter(description = "Envío a crear") @RequestBody Envio envio) {
        return ResponseEntity.status(HttpStatus.CREATED).body(envioService.save(envio));
    }

    @Operation(summary = "Actualizar envío por ID", description = "Actualiza los datos de un envío existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Envío actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnvioDTO.class))),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado",
            content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EnvioDTO> updateById(
            @Parameter(description = "ID del envío a actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del envío") @RequestBody Envio envio){
        return ResponseEntity.status(HttpStatus.OK).body(envioService.updateById(id, envio));
    }

    @Operation(summary = "Eliminar envío por ID", description = "Elimina un envío existente por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Envío eliminado exitosamente",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado",
            content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID del envío a eliminar", example = "1") @PathVariable Long id){
        envioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/costoEnvio")
    public ResponseEntity<BigDecimal> getCostoEnvio(){
        return ResponseEntity.status(HttpStatus.OK).body(envioService.getCostoEnvio());
    }

    @PutMapping("/actualizarEstado/{idPedido}/{nuevoEstado}")
    public ResponseEntity<String>  updateEstadoByIdPedido(@PathVariable Long idPedido, @PathVariable String nuevoEstado){
        return ResponseEntity.status(HttpStatus.OK).body(envioService.updateEstadoById(idPedido, nuevoEstado));
    }
}
