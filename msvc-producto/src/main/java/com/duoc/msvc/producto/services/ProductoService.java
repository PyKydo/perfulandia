package com.duoc.msvc.producto.services;

import com.duoc.msvc.producto.dtos.ProductoDTO;
import com.duoc.msvc.producto.dtos.ProductoHateoasDTO;
import com.duoc.msvc.producto.models.entities.Producto;
import org.springframework.hateoas.CollectionModel;

import java.util.List;

public interface ProductoService {
    // Versión simple
    List<ProductoDTO> findAllSimple();
    List<ProductoDTO> findByCategoriaSimple(String categoria);
    List<ProductoDTO> findByMarcaSimple(String marca);

    // Versión HATEOAS
    CollectionModel<ProductoHateoasDTO> findAllHateoas();
    CollectionModel<ProductoHateoasDTO> findByCategoria(String categoria);
    CollectionModel<ProductoHateoasDTO> findByMarca(String marca);
    ProductoHateoasDTO findById(Long id);
    ProductoHateoasDTO save(Producto producto);
    ProductoHateoasDTO updateById(Long id, Producto producto);
    void deleteById(Long id);
}
