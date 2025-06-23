package com.duoc.msvc.pago.controllers;

import com.duoc.msvc.pago.clients.PedidoClient;
import com.duoc.msvc.pago.dtos.PagoDTO;
import com.duoc.msvc.pago.dtos.pojos.PedidoClientDTO;
import com.duoc.msvc.pago.exceptions.PagoException;
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
    @Autowired
    private PedidoClient pedidoClient;

    @GetMapping
    public ResponseEntity<List<PagoDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(this.pagoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(this.pagoService.findById(id));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PagoDTO>> findByEstado(@PathVariable String estado){
        return ResponseEntity.status(HttpStatus.OK).body(this.pagoService.findByEstado(estado));
    }

    @PostMapping
    public ResponseEntity<PagoDTO> save(@Valid @RequestBody Pago pago){
        ResponseEntity<PedidoClientDTO> response = this.pedidoClient.findById(pago.getIdPedido());

        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new PagoException("El pedido con ID " + pago.getIdPedido() + " no existe");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.save(pago));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoDTO> updateById(@PathVariable Long id, @Valid @RequestBody Pago pago){
        return ResponseEntity.status(HttpStatus.OK).body(pagoService.updateById(id, pago));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        pagoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/actualizarEstado/{idPedido}/{nuevoEstado}")
    public ResponseEntity<String> updateEstadoByIdPedido(@PathVariable Long idPedido, @PathVariable String nuevoEstado){
        return ResponseEntity.status(HttpStatus.OK).body(pagoService.updateEstadoById(idPedido, nuevoEstado));
    }
}
