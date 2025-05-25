package com.duoc.msvc.sucursal.repositories;

import com.duoc.msvc.sucursal.models.entities.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
}
