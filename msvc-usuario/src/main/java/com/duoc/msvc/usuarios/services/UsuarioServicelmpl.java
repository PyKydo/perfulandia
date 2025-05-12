package com.duoc.msvc.usuarios.services;

import com.duoc.msvc.usuarios.exceptions.UsuarioException;
import com.duoc.msvc.usuarios.models.Usuario;
import com.duoc.msvc.usuarios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServicelmpl implements UsuarioService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> findAll() { return this.usuarioRepository.findAll(); }

    @Override
    public Usuario findById(Long id) {
        return this.usuarioRepository.findById(id).orElseThrow(
                () -> new UsuarioException("El usuario con id"+ id+ "no se encuentra registrado")
        );
    }

    @Override
    public  Usuario save(Usuario usuario){
        try{

        }
    }
}
