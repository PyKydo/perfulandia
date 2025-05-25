package com.duoc.msvc.sucursal.controllers;


import com.duoc.msvc.sucursal.dtos.SucursalDTO;
import com.duoc.msvc.sucursal.models.entities.Sucursal;
import com.duoc.msvc.sucursal.services.SucursalService;
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

    @GetMapping
    public ResponseEntity<List<SucursalDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(this.sucursalService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalDTO> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(this.sucursalService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SucursalDTO> save(@Valid @RequestBody Sucursal sucursal){
        if (sucursal.getInventarios() != null) {
            sucursal.getInventarios().forEach(inventario -> inventario.setSucursal(sucursal));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.sucursalService.save(sucursal));
    }
}
