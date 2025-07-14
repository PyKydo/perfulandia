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
    WHERE i.id_producto = :idProducto AND i.stock > 0
    ORDER BY i.stock DESC
    LIMIT 1
""", nativeQuery = true)
    Sucursal findBySucursalBestStock(@Param("idProducto") Long idProducto);

    @Query(value = """
    SELECT COUNT(i.id_inventario)
    FROM inventarios i
    WHERE i.id_producto = :idProducto
""", nativeQuery = true)
    Long countInventariosByProducto(@Param("idProducto") Long idProducto);

    @Query(value = """
    SELECT COUNT(i.id_inventario)
    FROM inventarios i
    WHERE i.id_producto = :idProducto AND i.stock > 0
""", nativeQuery = true)
    Long countInventariosWithStockByProducto(@Param("idProducto") Long idProducto);
}
