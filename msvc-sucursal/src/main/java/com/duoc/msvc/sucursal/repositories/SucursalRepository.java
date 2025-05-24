package com.duoc.msvc.sucursal.repositories;

import com.duoc.msvc.sucursal.models.Inventario;
import com.duoc.msvc.sucursal.models.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    @Query("SELECT i FROM InventarioSucursal i WHERE i.sucursal.id = :id")
    List<Inventario> findInventariosByIdSucursal(Long id);
}
