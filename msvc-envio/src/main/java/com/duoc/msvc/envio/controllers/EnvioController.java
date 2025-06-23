package com.duoc.msvc.envio.controllers;


import com.duoc.msvc.envio.dtos.EnvioDTO;
import com.duoc.msvc.envio.models.entities.Envio;
import com.duoc.msvc.envio.services.EnvioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/envios")
@Validated
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @GetMapping
    public ResponseEntity<List<EnvioDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(envioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnvioDTO> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(envioService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EnvioDTO> save(@Valid @RequestBody Envio envio){
        return ResponseEntity.status(HttpStatus.CREATED).body(envioService.save(envio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnvioDTO> updateById(@PathVariable Long id, @Valid @RequestBody Envio envio){
        return ResponseEntity.status(HttpStatus.OK).body(envioService.updateById(id, envio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        envioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/costoEnvio")
    public ResponseEntity<BigDecimal> getCostoEnvio(){
        return ResponseEntity.status(HttpStatus.OK).body(envioService.getCostoEnvio());
    }

    @PutMapping("/actualizarEstado/{idPedido}/{nuevoEstado}")
    public ResponseEntity<String>  updateEstadoByIdPedido(@PathVariable Long idPedido, @PathVariable String nuevoEstado){
        return ResponseEntity.status(HttpStatus.OK).body(envioService.updateEstadoById(idPedido, nuevoEstado));
    }
}
