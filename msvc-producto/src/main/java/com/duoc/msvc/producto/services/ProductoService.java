package com.duoc.msvc.producto.services;

import com.duoc.msvc.producto.models.entities.Producto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public interface ProductoService {
    CollectionModel<EntityModel<Producto>> findAll();
    CollectionModel<EntityModel<Producto>> findByCategoria(String categoria);
    CollectionModel<EntityModel<Producto>> findByMarca(String marca);
    EntityModel<Producto> findById(Long id);
    EntityModel<Producto> save(Producto producto);
    EntityModel<Producto> updateById(Long id, Producto producto);
    void deleteById(Long id);
}
