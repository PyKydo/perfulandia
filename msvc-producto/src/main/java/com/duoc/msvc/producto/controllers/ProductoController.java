package com.duoc.msvc.producto.controllers;

import com.duoc.msvc.producto.dtos.ProductoDTO;
import com.duoc.msvc.producto.dtos.ProductoHateoasDTO;
import com.duoc.msvc.producto.models.entities.Producto;
import com.duoc.msvc.producto.services.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@Validated // Valida que tenga los atributos obligatorios
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @GetMapping("/simple")
    @io.swagger.v3.oas.annotations.Operation(summary = "Obtener todos los productos (simple)", description = "Retorna una lista simple de productos sin enlaces HATEOAS", tags = {"Productos"})
    public ResponseEntity<List<ProductoDTO>> findAllSimple(){
        return ResponseEntity.status(HttpStatus.OK).body(this.productoService.findAllSimple());
    }

    @GetMapping
    @io.swagger.v3.oas.annotations.Operation(summary = "Obtener todos los productos (HATEOAS)", description = "Retorna una lista de productos con enlaces HATEOAS", tags = {"Productos"})
    public ResponseEntity<CollectionModel<ProductoHateoasDTO>> findAllHateoas(){
        return ResponseEntity.status(HttpStatus.OK).body(this.productoService.findAllHateoas());
    }

    @GetMapping("/categoria/simple/{categoria}")
    public ResponseEntity<List<ProductoDTO>> findByCategoriaSimple(@PathVariable String categoria){
        return ResponseEntity.status(HttpStatus.OK).body(this.productoService.findByCategoriaSimple(categoria));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<CollectionModel<ProductoHateoasDTO>> findByCategoria(@PathVariable String categoria){
        return ResponseEntity.status(HttpStatus.OK).body(this.productoService.findByCategoria(categoria));
    }

    @GetMapping("/marca/simple/{marca}")
    public ResponseEntity<List<ProductoDTO>> findByMarcaSimple(@PathVariable String marca){
        return ResponseEntity.status(HttpStatus.OK).body(this.productoService.findByMarcaSimple(marca));
    }

    @GetMapping("/marca/{marca}")
    public ResponseEntity<CollectionModel<ProductoHateoasDTO>> findByMarca(@PathVariable String marca){
        return ResponseEntity.status(HttpStatus.OK).body(this.productoService.findByMarca(marca));
    }

    @Operation(summary = "Obtener producto por ID", description = "Retorna un producto dado su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoHateoasDTO> findById(
            @Parameter(description = "ID del producto", example = "1") @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.productoService.findById(id));
    }

    @Operation(summary = "Crear un nuevo producto", description = "Crea un producto a partir de los datos enviados en el cuerpo de la petición")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProductoHateoasDTO> save(
            @Parameter(description = "Producto a crear") @RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.productoService.save(producto));
    }

    @Operation(summary = "Actualizar producto por ID", description = "Actualiza los datos de un producto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoHateoasDTO> updateById(
            @Parameter(description = "ID del producto a actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del producto") @RequestBody Producto producto){
        return ResponseEntity.status(HttpStatus.OK).body(this.productoService.updateById(id, producto));
    }

    @Operation(summary = "Eliminar producto por ID", description = "Elimina un producto existente por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID del producto a eliminar", example = "1") @PathVariable Long id){
        this.productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
