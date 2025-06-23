package com.duoc.msvc.producto.services;

import com.duoc.msvc.producto.dtos.ProductoDTO;
import com.duoc.msvc.producto.exceptions.ProductoException;
import com.duoc.msvc.producto.models.entities.Producto;
import com.duoc.msvc.producto.repositories.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService{
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<ProductoDTO> findAll() {
        return this.productoRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    @Override
    public ProductoDTO findById(Long id) {
        Producto producto = this.productoRepository.findById(id).orElseThrow(
                () -> new ProductoException("El producto con el id " + id + " no existe")
        );

        return convertToDTO(producto);
    }
    @Override
    public List<ProductoDTO> findByCategoria(String categoria) {
        return this.productoRepository.findByCategoria(categoria).stream().map(this::convertToDTO).toList();
    }

    @Transactional
    @Override
    public ProductoDTO save(Producto producto) {
        return convertToDTO(this.productoRepository.save(producto));
    }

    @Transactional
    @Override
    public ProductoDTO updateById(Long id, Producto producto) {
        Producto productoDb = productoRepository.findById(id).orElseThrow(
            () -> new ProductoException("El producto con el id " + id + " no existe en la base de datos")
        );

        productoDb.setNombre(producto.getNombre());
        productoDb.setMarca(producto.getMarca());
        productoDb.setPrecio(producto.getPrecio());
        productoDb.setDescripcion(producto.getDescripcion());
        productoDb.setImagenRepresentativaURL(producto.getImagenRepresentativaURL());
        productoDb.setCategoria(producto.getCategoria());
        productoDb.setPorcentajeConcentracion(producto.getPorcentajeConcentracion());
        
        return convertToDTO(productoRepository.save(productoDb));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        productoRepository.findById(id).orElseThrow(
            () -> new ProductoException("El producto con el id " + id + " no existe en la base de datos")
        );
        productoRepository.deleteById(id);
    }

    @Override
    public ProductoDTO convertToDTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getIdProducto());
        dto.setNombre(producto.getNombre());
        dto.setMarca(producto.getMarca());
        dto.setPrecio(producto.getPrecio());
        dto.setDescripcion(producto.getDescripcion());
        dto.setImagenRepresentativaURL(producto.getImagenRepresentativaURL());
        dto.setCategoria(producto.getCategoria());
        dto.setPorcentajeConcentracion(producto.getPorcentajeConcentracion());
        return dto;
    }
}
