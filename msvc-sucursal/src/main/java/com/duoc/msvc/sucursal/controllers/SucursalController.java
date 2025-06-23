package com.duoc.msvc.sucursal.controllers;

import com.duoc.msvc.sucursal.dtos.SucursalDTO;
import com.duoc.msvc.sucursal.models.entities.Sucursal;
import com.duoc.msvc.sucursal.services.SucursalService;
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
@RequestMapping("/api/v1/sucursales")
@Validated
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @Operation(summary = "Obtener todas las sucursales", description = "Retorna una lista de todas las sucursales registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de sucursales obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<SucursalDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(this.sucursalService.findAll());
    }

    @Operation(summary = "Obtener sucursal por ID", description = "Retorna una sucursal dado su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalDTO.class))),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<SucursalDTO> findById(
            @Parameter(description = "ID de la sucursal", example = "1") @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.sucursalService.findById(id));
    }

    @Operation(summary = "Crear una nueva sucursal", description = "Crea una sucursal a partir de los datos enviados en el cuerpo de la petición")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucursal creada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<SucursalDTO> save(
            @Parameter(description = "Sucursal a crear") @RequestBody Sucursal sucursal) {
        if (sucursal.getInventarios() != null) {
            sucursal.getInventarios().forEach(inventario -> inventario.setSucursal(sucursal));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.sucursalService.save(sucursal));
    }

    @Operation(summary = "Actualizar sucursal por ID", description = "Actualiza los datos de una sucursal existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal actualizada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalDTO.class))),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada",
            content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<SucursalDTO> updateById(
            @Parameter(description = "ID de la sucursal a actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Datos actualizados de la sucursal") @RequestBody Sucursal sucursal){
        return ResponseEntity.status(HttpStatus.OK).body(this.sucursalService.updateById(id, sucursal));
    }

    @PutMapping("/{idSuc}/inventario/{idInv}/stock")
    public ResponseEntity<Void> updateInventarioStock(
            @PathVariable("idSuc") Long idSucursal,
            @PathVariable("idInv") Long idInventario,
            @RequestParam("nuevoStock") Integer nuevoStock
    ) {
        sucursalService.updateStock(idSucursal, idInventario, nuevoStock);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar sucursal por ID", description = "Elimina una sucursal existente por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sucursal eliminada exitosamente",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada",
            content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID de la sucursal a eliminar", example = "1") @PathVariable Long id){
        this.sucursalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mejor-stock/{idProducto}")
    public ResponseEntity<SucursalDTO> bestStockByIdProducto(@PathVariable Long idProducto){
        return ResponseEntity.status(HttpStatus.OK).body(sucursalService.findByBestStock(idProducto));
    }
}
