package com.duoc.msvc.envio.repositories;

import com.duoc.msvc.envio.models.entities.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    Envio findByIdPedido(Long idPedido);
}
