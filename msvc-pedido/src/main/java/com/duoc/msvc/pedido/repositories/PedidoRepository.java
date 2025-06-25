package com.duoc.msvc.pedido.repositories;

import com.duoc.msvc.pedido.models.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByIdCliente(Long idCliente);

    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.detallesPedido")
    List<Pedido> findAllWithDetalles();
}
