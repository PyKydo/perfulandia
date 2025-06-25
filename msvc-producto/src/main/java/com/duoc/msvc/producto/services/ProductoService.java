package com.duoc.msvc.producto.services;

import com.duoc.msvc.producto.dtos.ProductoHateoasDTO;
import com.duoc.msvc.producto.models.entities.Producto;
import org.springframework.hateoas.CollectionModel;

public interface ProductoService {
    CollectionModel<ProductoHateoasDTO> findAll();
    CollectionModel<ProductoHateoasDTO> findByCategoria(String categoria);
    CollectionModel<ProductoHateoasDTO> findByMarca(String marca);
    ProductoHateoasDTO findById(Long id);
    ProductoHateoasDTO save(Producto producto);
    ProductoHateoasDTO updateById(Long id, Producto producto);
    void deleteById(Long id);
}
