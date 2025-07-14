package com.duoc.msvc.sucursal.controllers;

import com.duoc.msvc.sucursal.dtos.SucursalCreateDTO;
import com.duoc.msvc.sucursal.dtos.SucursalUpdateDTO;
import com.duoc.msvc.sucursal.services.SucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import com.duoc.msvc.sucursal.models.entities.Sucursal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@RestController
@RequestMapping("/api/v1/sucursales-hateoas")
@Validated
@Tag(name = "2. Sucursal (HATEOAS)", description = "Endpoints para gestión de sucursales con HATEOAS. Las respuestas incluyen enlaces para navegación RESTful.")
public class SucursalHateoasController {
    @Autowired
    private SucursalService sucursalService;

    @GetMapping("/test")
    @Operation(summary = "Test HATEOAS", description = "Endpoint de prueba para verificar que los enlaces HATEOAS funcionan.")
    public ResponseEntity<EntityModel<String>> testHateoas() {
        EntityModel<String> entityModel = EntityModel.of("Test HATEOAS funcionando",
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SucursalHateoasController.class).testHateoas()).withSelfRel(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SucursalHateoasController.class).findAll()).withRel("sucursales"),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SucursalHateoasController.class).save(null)).withRel("create")
        );
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las sucursales (HATEOAS)", description = "Retorna una lista de sucursales con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de sucursales obtenida exitosamente",
            content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<CollectionModel<EntityModel<Sucursal>>> findAll() {
        CollectionModel<EntityModel<Sucursal>> collectionModel = sucursalService.findAllHateoas();
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener sucursal por ID (HATEOAS)", description = "Retorna una sucursal por su ID con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal encontrada",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada",
            content = @Content)
    })
    public ResponseEntity<EntityModel<Sucursal>> findById(
            @Parameter(description = "ID de la sucursal", example = "1") @PathVariable Long id) {
        EntityModel<Sucursal> entityModel = sucursalService.findByIdHateoas(id);
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva sucursal (HATEOAS)", description = "Crea una sucursal y retorna la entidad con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucursal creada exitosamente",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    public ResponseEntity<EntityModel<Sucursal>> save(@RequestBody @Valid SucursalCreateDTO sucursalCreateDTO) {
        var sucursalGetDTO = sucursalService.save(sucursalCreateDTO);
        EntityModel<Sucursal> entityModel = sucursalService.findByIdHateoas(sucursalGetDTO.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar sucursal por ID (HATEOAS)", description = "Actualiza una sucursal existente y retorna la entidad con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal actualizada exitosamente",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada",
            content = @Content)
    })
    public ResponseEntity<EntityModel<Sucursal>> updateById(
            @Parameter(description = "ID de la sucursal a actualizar", example = "1") @PathVariable Long id,
            @RequestBody @Valid SucursalUpdateDTO sucursalUpdateDTO) {
        var sucursalGetDTO = sucursalService.updateById(id, sucursalUpdateDTO);
        EntityModel<Sucursal> entityModel = sucursalService.findByIdHateoas(id);
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
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
    public ResponseEntity<EntityModel<Sucursal>> bestStockByIdProducto(@PathVariable Long idProducto){
        var sucursalGetDTO = sucursalService.findByBestStock(idProducto);
        EntityModel<Sucursal> entityModel = sucursalService.findByIdHateoas(sucursalGetDTO.getId());
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    @GetMapping("/disponibilidad/{idProducto}")
    @Operation(summary = "Consultar disponibilidad de producto (HATEOAS)", description = "Obtiene información sobre la disponibilidad de un producto en las sucursales")
    public ResponseEntity<String> getProductoDisponibilidad(@PathVariable Long idProducto){
        String disponibilidad = sucursalService.getProductoDisponibilidad(idProducto);
        return ResponseEntity.status(HttpStatus.OK).body(disponibilidad);
    }


} 