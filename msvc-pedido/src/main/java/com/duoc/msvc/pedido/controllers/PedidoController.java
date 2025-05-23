package com.duoc.msvc.pedido.controllers;

import com.duoc.msvc.pedido.dtos.PedidoDTO;
import com.duoc.msvc.pedido.models.entities.Pedido;
import com.duoc.msvc.pedido.services.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
@Validated
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(this.pedidoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(this.pedidoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> save(@Valid @RequestBody Pedido pedido){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.pedidoService.save(pedido));
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<PedidoDTO>> findByIdCliente(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(this.pedidoService.findByIdCliente(id));
    }



}
