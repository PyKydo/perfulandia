package com.duoc.msvc.sucursal.controllers;

import com.duoc.msvc.sucursal.dtos.SucursalHateoasDTO;
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
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sucursales-hateoas")
@Validated
@Tag(name = "Sucursal (HATEOAS)", description = "Endpoints para gestión de sucursales con respuestas HATEOAS. Ejemplo de respuesta:")
public class SucursalHATEOASController {
    @Autowired
    private SucursalService sucursalService;

    @GetMapping
    @Operation(summary = "Obtener todas las sucursales (HATEOAS)", description = "Retorna una lista de sucursales con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de sucursales obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalHateoasDTO.class)))
    })
    public ResponseEntity<CollectionModel<SucursalHateoasDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(sucursalService.findAllHateoas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener sucursal por ID (HATEOAS)", description = "Retorna una sucursal por su ID con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalHateoasDTO.class))),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada",
            content = @Content)
    })
    public ResponseEntity<SucursalHateoasDTO> findById(
            @Parameter(description = "ID de la sucursal", example = "1") @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(sucursalService.findByIdHateoas(id));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva sucursal (HATEOAS)", description = "Crea una sucursal y retorna la entidad con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucursal creada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalHateoasDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    public ResponseEntity<SucursalHateoasDTO> save(@RequestBody @Valid SucursalCreateDTO sucursalCreateDTO) {
        // Guardar y devolver con enlaces HATEOAS
        var sucursal = sucursalService.save(sucursalCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(sucursalService.findByIdHateoas(sucursal.getId()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar sucursal por ID (HATEOAS)", description = "Actualiza una sucursal existente y retorna la entidad con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal actualizada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalHateoasDTO.class))),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada",
            content = @Content)
    })
    public ResponseEntity<SucursalHateoasDTO> updateById(
            @Parameter(description = "ID de la sucursal a actualizar", example = "1") @PathVariable Long id,
            @RequestBody @Valid SucursalUpdateDTO sucursalUpdateDTO) {
        var sucursal = sucursalService.updateById(id, sucursalUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(sucursalService.findByIdHateoas(sucursal.getId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar sucursal por ID (HATEOAS)", description = "Elimina una sucursal existente por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sucursal eliminada exitosamente",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada",
            content = @Content)
    })
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID de la sucursal a eliminar", example = "1") @PathVariable Long id){
        this.sucursalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idSuc}/inventario/{idInv}/stock")
    @Operation(summary = "Actualizar stock de inventario en sucursal (HATEOAS)", description = "Actualiza el stock de un inventario en una sucursal específica.")
    public ResponseEntity<Void> updateInventarioStock(
            @PathVariable("idSuc") Long idSucursal,
            @PathVariable("idInv") Long idInventario,
            @RequestParam("nuevoStock") Integer nuevoStock
    ) {
        sucursalService.updateStock(idSucursal, idInventario, nuevoStock);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mejor-stock/{idProducto}")
    @Operation(summary = "Obtener sucursal con mejor stock para un producto (HATEOAS)", description = "Retorna la sucursal con mayor stock disponible para un producto, con enlaces HATEOAS.")
    public ResponseEntity<SucursalHateoasDTO> bestStockByIdProducto(@PathVariable Long idProducto){
        var sucursal = sucursalService.findByBestStock(idProducto);
        return ResponseEntity.status(HttpStatus.OK).body(sucursalService.findByIdHateoas(sucursal.getId()));
    }
} 