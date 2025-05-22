package com.duoc.msvc.usuario.services;


import com.duoc.msvc.usuario.exceptions.UsuarioException;
import com.duoc.msvc.usuario.models.entities.Usuario;
import com.duoc.msvc.usuario.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UsuarioServicelmpl implements UsuarioService{

    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> findAll() {return this.usuarioRepository.findAll();}


    @Override
    public Usuario findById(Long id){
        return this.usuarioRepository.findById(id).orElseThrow(
                () -> new UsuarioException("El usuario con id "+id+" no se encuentra en la base de datos")
        );
    }

    @Override
    public Usuario save(Usuario usuario) {
        return null;
    }
}
