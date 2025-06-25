package com.duoc.msvc.sucursal.controllers;

import com.duoc.msvc.sucursal.dtos.SucursalGetDTO;
import com.duoc.msvc.sucursal.dtos.SucursalCreateDTO;
import com.duoc.msvc.sucursal.dtos.SucursalUpdateDTO;
import com.duoc.msvc.sucursal.services.SucursalService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/sucursales")
@Validated
@Tag(name = "Sucursal (Simple)", description = "Endpoints para gestión de sucursales sin HATEOAS. Respuestas simples, ideales para clientes que no requieren enlaces.")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @Operation(summary = "Obtener todas las sucursales", description = "Retorna una lista de todas las sucursales registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de sucursales obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalGetDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<SucursalGetDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(this.sucursalService.findAll());
    }

    @Operation(summary = "Obtener sucursal por ID", description = "Retorna una sucursal dado su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalGetDTO.class))),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<SucursalGetDTO> findById(
            @Parameter(description = "ID de la sucursal", example = "1") @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.sucursalService.findById(id));
    }

    @Operation(summary = "Crear una nueva sucursal", description = "Crea una sucursal a partir de los datos enviados en el cuerpo de la petición")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucursal creada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalGetDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<SucursalGetDTO> save(
            @Parameter(description = "Sucursal a crear") @RequestBody @Valid SucursalCreateDTO sucursalCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.sucursalService.save(sucursalCreateDTO));
    }

    @Operation(summary = "Actualizar sucursal por ID", description = "Actualiza los datos de una sucursal existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal actualizada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalGetDTO.class))),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada",
            content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<SucursalGetDTO> updateById(
            @Parameter(description = "ID de la sucursal a actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Datos actualizados de la sucursal") @RequestBody @Valid SucursalUpdateDTO sucursalUpdateDTO){
        return ResponseEntity.status(HttpStatus.OK).body(this.sucursalService.updateById(id, sucursalUpdateDTO));
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
    public ResponseEntity<SucursalGetDTO> bestStockByIdProducto(@PathVariable Long idProducto){
        return ResponseEntity.status(HttpStatus.OK).body(sucursalService.findByBestStock(idProducto));
    }
}
