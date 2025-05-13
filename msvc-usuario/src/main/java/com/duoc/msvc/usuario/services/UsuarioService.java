package com.duoc.msvc.usuario.services;

import com.duoc.msvc.usuario.models.entities.Usuario;

import java.util.List;

public interface UsuarioService {
    List<Usuario> findAll();
    Usuario findById(Long id);
    Usuario save(Usuario usuario);
}
