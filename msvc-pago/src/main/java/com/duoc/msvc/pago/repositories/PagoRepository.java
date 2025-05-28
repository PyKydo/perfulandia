package com.duoc.msvc.pago.repositories;

import com.duoc.msvc.pago.models.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByEstado(String estado);
    Pago findByIdPedido(Long id);

}
