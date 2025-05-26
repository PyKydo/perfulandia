package com.duoc.msvc.sucursal.repositories;

import com.duoc.msvc.sucursal.models.entities.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    @Query(value = """
    SELECT s.*
    FROM inventarios i
    INNER JOIN sucursales s ON i.id_sucursal = s.id_sucursal
    WHERE i.id_producto = :idProducto
    ORDER BY i.stock DESC
    LIMIT 1
""", nativeQuery = true)
    Sucursal findBySucursalBestStock(@Param("idProducto") Long idProducto);
}
