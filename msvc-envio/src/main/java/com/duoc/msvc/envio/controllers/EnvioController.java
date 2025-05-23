package com.duoc.msvc.envio.controllers;


import com.duoc.msvc.envio.models.entities.Envio;
import com.duoc.msvc.envio.services.EnvioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@Validated
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @GetMapping
    public ResponseEntity<List<Envio>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(envioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Envio> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(envioService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Envio> save(@Valid @RequestBody Envio envio){
        return ResponseEntity.status(HttpStatus.CREATED).body(envioService.save(envio));
    }
}
