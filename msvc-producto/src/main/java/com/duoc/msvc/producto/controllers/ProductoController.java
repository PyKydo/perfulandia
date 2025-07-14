package com.duoc.msvc.producto.controllers;

import com.duoc.msvc.producto.dtos.ProductoGetDTO;
import com.duoc.msvc.producto.dtos.ProductoCreateDTO;
import com.duoc.msvc.producto.dtos.ProductoUpdateDTO;
import com.duoc.msvc.producto.models.entities.Producto;
import com.duoc.msvc.producto.services.ProductoService;
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

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.EntityModel;

@RestController
@RequestMapping("/api/v1/productos")
@Validated
@Tag(name = "1. Producto (Simple)", description = "Endpoints para gestión de productos sin HATEOAS. Respuestas simples, ideales para clientes que no requieren enlaces.")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Obtener todos los productos (simple)", description = "Retorna una lista simple de productos sin enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoGetDTO.class)))
    })
    public ResponseEntity<List<ProductoGetDTO>> findAll(){
        var collectionModel = this.productoService.findAll();
        List<ProductoGetDTO> productos = collectionModel.getContent().stream()
            .map(entityModel -> {
                Producto producto = entityModel.getContent();
                return convertToGetDTO(producto);
            })
            .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(productos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID (simple)", description = "Retorna un producto por su ID sin enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoGetDTO.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content)
    })
    public ResponseEntity<ProductoGetDTO> findById(
            @Parameter(description = "ID único del producto", example = "1") @PathVariable Long id) {
        var entityModel = this.productoService.findById(id);
        Producto producto = entityModel.getContent();
        return ResponseEntity.status(HttpStatus.OK).body(convertToGetDTO(producto));
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Obtener productos por categoría (simple)", description = "Retorna productos filtrados por categoría sin enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos encontrados",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoGetDTO.class)))
    })
    public ResponseEntity<List<ProductoGetDTO>> findByCategoria(
            @Parameter(description = "Nombre de la categoría", example = "Perfumes") @PathVariable String categoria){
        var collectionModel = this.productoService.findByCategoria(categoria);
        List<ProductoGetDTO> productos = collectionModel.getContent().stream()
            .map(entityModel -> {
                Producto producto = entityModel.getContent();
                return convertToGetDTO(producto);
            })
            .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(productos);
    }

    @GetMapping("/marca/{marca}")
    @Operation(summary = "Obtener productos por marca (simple)", description = "Retorna productos filtrados por marca sin enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos encontrados",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoGetDTO.class)))
    })
    public ResponseEntity<List<ProductoGetDTO>> findByMarca(
            @Parameter(description = "Nombre de la marca", example = "Chanel") @PathVariable String marca){
        var collectionModel = this.productoService.findByMarca(marca);
        List<ProductoGetDTO> productos = collectionModel.getContent().stream()
            .map(entityModel -> {
                Producto producto = entityModel.getContent();
                return convertToGetDTO(producto);
            })
            .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(productos);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto (simple)", description = "Crea un producto a partir de los datos enviados en el cuerpo de la petición.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoGetDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    public ResponseEntity<ProductoGetDTO> save(
            @Parameter(description = "Datos del producto a crear") @RequestBody @Validated ProductoCreateDTO productoCreateDTO) {
        Producto producto = convertToEntity(productoCreateDTO);
        var entityModel = this.productoService.save(producto);
        Producto prod = entityModel.getContent();
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToGetDTO(prod));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto por ID (simple)", description = "Actualiza los datos de un producto existente sin enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoGetDTO.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content)
    })
    public ResponseEntity<ProductoGetDTO> updateById(
            @Parameter(description = "ID único del producto a actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del producto") @RequestBody @Validated ProductoUpdateDTO productoUpdateDTO){
        Producto producto = convertToEntity(productoUpdateDTO);
        var entityModel = this.productoService.updateById(id, producto);
        Producto prod = entityModel.getContent();
        return ResponseEntity.status(HttpStatus.OK).body(convertToGetDTO(prod));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto por ID (simple)", description = "Elimina un producto existente por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content)
    })
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID único del producto a eliminar", example = "1") @PathVariable Long id){
        this.productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ProductoGetDTO convertToGetDTO(Producto producto) {
        ProductoGetDTO dto = new ProductoGetDTO();
        dto.setId(producto.getIdProducto());
        dto.setNombre(producto.getNombre());
        dto.setMarca(producto.getMarca());
        dto.setPrecio(producto.getPrecio());
        dto.setDescripcion(producto.getDescripcion());
        dto.setImagenRepresentativaURL(producto.getImagenRepresentativaURL());
        dto.setCategoria(producto.getCategoria());
        dto.setPorcentajeConcentracion(producto.getPorcentajeConcentracion());
        return dto;
    }

    private Producto convertToEntity(ProductoCreateDTO dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setMarca(dto.getMarca());
        producto.setPrecio(dto.getPrecio());
        producto.setDescripcion(dto.getDescripcion());
        producto.setImagenRepresentativaURL(dto.getImagenRepresentativaURL());
        producto.setCategoria(dto.getCategoria());
        producto.setPorcentajeConcentracion(dto.getPorcentajeConcentracion());
        return producto;
    }

    private Producto convertToEntity(ProductoUpdateDTO dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setMarca(dto.getMarca());
        producto.setPrecio(dto.getPrecio());
        producto.setDescripcion(dto.getDescripcion());
        producto.setImagenRepresentativaURL(dto.getImagenRepresentativaURL());
        producto.setCategoria(dto.getCategoria());
        producto.setPorcentajeConcentracion(dto.getPorcentajeConcentracion());
        return producto;
    }
}
