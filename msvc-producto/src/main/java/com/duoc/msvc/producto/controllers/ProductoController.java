package com.duoc.msvc.producto.controllers;

import com.duoc.msvc.producto.dtos.ProductoDTO;
import com.duoc.msvc.producto.models.entities.Producto;
import com.duoc.msvc.producto.services.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(this.productoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(this.productoService.findById(id));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoDTO>> findByCategoria(@PathVariable String categoria){
        return ResponseEntity.status(HttpStatus.OK).body(this.productoService.findByCategoria(categoria));
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> save(@Valid @RequestBody Producto producto){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.productoService.save(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> updateById(@PathVariable Long id, @RequestBody Producto producto){
        return ResponseEntity.status(HttpStatus.OK).body(this.productoService.updateById(id, producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        this.productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
