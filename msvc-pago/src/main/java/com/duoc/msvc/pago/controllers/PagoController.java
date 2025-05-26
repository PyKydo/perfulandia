package com.duoc.msvc.pago.controllers;

import com.duoc.msvc.pago.dtos.PagoDTO;
import com.duoc.msvc.pago.models.Pago;
import com.duoc.msvc.pago.services.PagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
@Validated
public class PagoController {
    @Autowired
    private PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<PagoDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(pagoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(pagoService.findById(id));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PagoDTO>> findByEstado(@PathVariable String estado){
        return ResponseEntity.status(HttpStatus.OK).body(pagoService.findByEstado(estado));
    }

    @PostMapping
    public ResponseEntity<PagoDTO> save(@Valid @RequestBody Pago pago){
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.save(pago));
    }
}
