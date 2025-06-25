package com.duoc.msvc.envio.controllers;

import com.duoc.msvc.envio.dtos.EnvioGetDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/envios")
@Validated
@Tag(name = "Envio (Simple)", description = "Endpoints para gestión de envíos con respuestas simples. Ejemplo de respuesta:")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @Operation(summary = "Obtener todos los envíos", description = "Retorna una lista de todos los envíos registrados (sin HATEOAS)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de envíos obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnvioGetDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<EnvioGetDTO>> findAll(){
        List<EnvioGetDTO> envios = envioService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(envios);
    }

    @Operation(summary = "Obtener envío por ID", description = "Retorna un envío dado su ID (sin HATEOAS)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Envío encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnvioGetDTO.class))),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EnvioGetDTO> findById(@PathVariable Long id) {
        EnvioGetDTO dto = envioService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Operation(summary = "Crear un nuevo envío", description = "Crea un envío a partir de los datos enviados en el cuerpo de la petición (sin HATEOAS)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Envío creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnvioGetDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<EnvioGetDTO> save(@Valid @RequestBody EnvioCreateDTO envioInput) {
        Envio envio = new Envio();
        envio.setIdPedido(envioInput.getIdPedido());
        envio.setDireccion(envioInput.getDireccion());
        envio.setRegion(envioInput.getRegion());
        envio.setCiudad(envioInput.getCiudad());
        envio.setComuna(envioInput.getComuna());
        envio.setCodigoPostal(envioInput.getCodigoPostal());
        envio.setCosto(envioInput.getCosto());
        EnvioGetDTO dto = envioService.save(envio);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Actualizar envío por ID", description = "Actualiza los datos de un envío existente (sin HATEOAS)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Envío actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnvioGetDTO.class))),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado",
            content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EnvioGetDTO> updateById(@PathVariable Long id, @Valid @RequestBody EnvioUpdateDTO envioUpdate){
        Envio envio = new Envio();
        envio.setDireccion(envioUpdate.getDireccion());
        envio.setRegion(envioUpdate.getRegion());
        envio.setCiudad(envioUpdate.getCiudad());
        envio.setComuna(envioUpdate.getComuna());
        envio.setCodigoPostal(envioUpdate.getCodigoPostal());
        envio.setEstado(envioUpdate.getEstado());
        envio.setFechaEstimadaEntrega(envioUpdate.getFechaEstimadaEntrega());
        EnvioGetDTO dto = envioService.updateById(id, envio);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
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
