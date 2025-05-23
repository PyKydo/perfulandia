package com.duoc.msvc.producto.services;

import com.duoc.msvc.producto.exceptions.ProductoException;
import com.duoc.msvc.producto.models.entities.Producto;
import com.duoc.msvc.producto.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService{
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> findAll() {
        return this.productoRepository.findAll();
    }

    @Override
    public Producto findById(Long id) {
        return this.productoRepository.findById(id).orElseThrow(
                () -> new ProductoException("El producto con el id " + id + " no existe en la base de datos")
        );
    }

    @Override
    public Producto save(Producto producto) {
        return this.productoRepository.save(producto);
    }
}
