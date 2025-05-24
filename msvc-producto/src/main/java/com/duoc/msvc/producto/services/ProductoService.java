package com.duoc.msvc.producto.services;

import com.duoc.msvc.producto.dtos.ProductoDTO;
import com.duoc.msvc.producto.models.entities.Producto;

import java.util.List;

public interface ProductoService {
    List<ProductoDTO> findAll();
    List<ProductoDTO> findByCategoria(String categoria);
    ProductoDTO findById(Long id);
    ProductoDTO save(Producto producto);
    ProductoDTO convertToDTO(Producto producto);
    // Producto update(Long id, Producto producto)
}
