package com.duoc.msvc.usuario.services;


import com.duoc.msvc.usuario.dtos.UsuarioDTO;
import com.duoc.msvc.usuario.exceptions.UsuarioException;
import com.duoc.msvc.usuario.models.entities.Usuario;
import com.duoc.msvc.usuario.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UsuarioServicelmpl implements UsuarioService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<UsuarioDTO> findAll(){
        return this.usuarioRepository.findAll().stream().map(this::convertToDTO).toList();
    }


    @Override
    public UsuarioDTO findById(Long id){
        Usuario usuario = this.usuarioRepository.findById(id).orElseThrow(
                () -> new UsuarioException("El usuario con id "+id+" no se encuentra en la base de datos")
        );

        return convertToDTO(usuario);
    }

    @Override
    public UsuarioDTO save(Usuario usuario) {
        return convertToDTO(this.usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioDTO updateById(Long id, Usuario usuario) {
        Usuario newUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El oldUsuario con el id "+id+" no se puede actualizar porque no existe"));

        newUsuario.setNombre(usuario.getNombre());
        newUsuario.setApellido(usuario.getApellido());
        newUsuario.setCorreo(usuario.getCorreo());
        newUsuario.setRol(usuario.getRol());
        newUsuario.setTelefono(usuario.getTelefono());

        return convertToDTO(usuarioRepository.save(newUsuario));
    }

    @Override
    public void deleteById(Long id) {
        this.usuarioRepository.deleteById(id);
    }

    @Override
    public UsuarioDTO convertToDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setCiudad(usuario.getCiudad());
        dto.setDireccion(usuario.getDireccion());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setCorreo(usuario.getCorreo());
        dto.setRol(usuario.getRol());
        dto.setTelefono(usuario.getTelefono());
        return dto;
    }
}
