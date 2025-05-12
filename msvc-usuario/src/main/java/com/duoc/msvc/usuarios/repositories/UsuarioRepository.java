package com.duoc.msvc.usuarios.repositories;

import com.duoc.msvc.usuarios.models.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
}
