package com.duoc.msvc.usuario.controllers;


import com.duoc.msvc.usuario.dtos.UsuarioDTO;
import com.duoc.msvc.usuario.dtos.pojos.PedidoClientDTO;
import com.duoc.msvc.usuario.models.entities.Usuario;
import com.duoc.msvc.usuario.services.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(this.usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.usuarioService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> save(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.usuarioService.save(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateById(@PathVariable Long id, @RequestBody Usuario usuario){
        return ResponseEntity.status(HttpStatus.OK).body(this.usuarioService.updateById(id, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        this.usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idCliente}/realizarPedido")
    public ResponseEntity<?> realizarPedido(@PathVariable Long idCliente, @RequestBody PedidoClientDTO pedidoClientDTO) {

        try {
            UsuarioDTO cliente = usuarioService.findById(idCliente);

            pedidoClientDTO.setIdCliente(idCliente);

            PedidoClientDTO pedidoRealizado = usuarioService.realizarPedido(pedidoClientDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoRealizado);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cliente con ID " + idCliente + " no encontrado");
        }
    }

    @GetMapping("{idCliente}/pagarPedido/{idPedido}")
    public ResponseEntity<PedidoClientDTO> pagarPedido(@PathVariable Long idCliente, @PathVariable Long idPedido){
        return ResponseEntity.status(HttpStatus.OK).body(this.usuarioService.pagarPedido(idCliente, idPedido));
    }

    @GetMapping("{idCliente}/misPedidos")
    public ResponseEntity<List<PedidoClientDTO>> obtenerMisPedidos(@PathVariable Long idCliente){
        return ResponseEntity.status(HttpStatus.OK).body(this.usuarioService.misPedidos(idCliente));
    }



}
