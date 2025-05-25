package com.duoc.msvc.usuario.controllers;


import com.duoc.msvc.usuario.dtos.UsuarioDTO;
import com.duoc.msvc.usuario.models.entities.Usuario;
import com.duoc.msvc.usuario.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }



}
