package com.duoc.msvc.producto.services;

import com.duoc.msvc.producto.models.entities.Producto;

import java.util.List;

public interface ProductoService {
    List<Producto> findAll();
    List<Producto> findByCategoria(String categoria);
    Producto findById(Long id);
    Producto save(Producto producto);
    // Producto update(Long id, Producto producto)
}
