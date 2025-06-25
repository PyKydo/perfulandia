package com.duoc.msvc.producto.controllers;

import com.duoc.msvc.producto.dtos.ProductoHateoasDTO;
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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/productos")
@Validated
@Tag(name = "Producto (HATEOAS)", description = "Endpoints para gestión de productos con HATEOAS. Las respuestas incluyen enlaces para navegación RESTful. Ejemplo de respuesta HATEOAS:\n{\n  \"id\": 1,\n  \"nombre\": \"Perfume Elegante\",\n  ...\n  \"_links\": {\n    \"self\": {\"href\": \"/api/v1/productos/1\"},\n    \"productos\": {\"href\": \"/api/v1/productos\"}\n  }\n}")
public class ProductoHATEOASController {
    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Obtener todos los productos (HATEOAS)", description = "Retorna una lista de productos con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoHateoasDTO.class)))
    })
    public ResponseEntity<CollectionModel<ProductoHateoasDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(this.productoService.findAll());
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Obtener productos por categoría (HATEOAS)", description = "Retorna productos filtrados por categoría con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos encontrados",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoHateoasDTO.class)))
    })
    public ResponseEntity<CollectionModel<ProductoHateoasDTO>> findByCategoria(
            @Parameter(description = "Nombre de la categoría", example = "Perfumes") @PathVariable String categoria){
        return ResponseEntity.status(HttpStatus.OK).body(this.productoService.findByCategoria(categoria));
    }

    @GetMapping("/marca/{marca}")
    @Operation(summary = "Obtener productos por marca (HATEOAS)", description = "Retorna productos filtrados por marca con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos encontrados",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoHateoasDTO.class)))
    })
    public ResponseEntity<CollectionModel<ProductoHateoasDTO>> findByMarca(
            @Parameter(description = "Nombre de la marca", example = "Chanel") @PathVariable String marca){
        return ResponseEntity.status(HttpStatus.OK).body(this.productoService.findByMarca(marca));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID (HATEOAS)", description = "Retorna un producto por su ID con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoHateoasDTO.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content)
    })
    public ResponseEntity<ProductoHateoasDTO> findById(
            @Parameter(description = "ID único del producto", example = "1") @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.productoService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto (HATEOAS)", description = "Crea un producto a partir de los datos enviados en el cuerpo de la petición.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoHateoasDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    public ResponseEntity<ProductoHateoasDTO> save(
            @Parameter(description = "Datos del producto a crear") @Valid @RequestBody ProductoCreateDTO productoCreateDTO) {
        Producto producto = convertToEntity(productoCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.productoService.save(producto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto por ID (HATEOAS)", description = "Actualiza los datos de un producto existente con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoHateoasDTO.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content)
    })
    public ResponseEntity<ProductoHateoasDTO> updateById(
            @Parameter(description = "ID único del producto a actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del producto") @Valid @RequestBody ProductoUpdateDTO productoUpdateDTO){
        Producto producto = convertToEntity(productoUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(this.productoService.updateById(id, producto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto por ID (HATEOAS)", description = "Elimina un producto existente por su ID.")
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