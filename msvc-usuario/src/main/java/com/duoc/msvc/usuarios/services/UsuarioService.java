package com.duoc.msvc.usuarios.services;

import com.duoc.msvc.usuarios.models.entities.Usuario;

import java.util.List;

public interface UsuarioService {
    List<Usuario> findAll();
    Usuario findById(Long id);
    Usuario save(Usuario usuario);
}
