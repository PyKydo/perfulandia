package com.duoc.msvc.producto.services;

import com.duoc.msvc.producto.assemblers.ProductoDTOModelAssembler;
import com.duoc.msvc.producto.dtos.ProductoDTO;
import com.duoc.msvc.producto.dtos.ProductoHateoasDTO;
import com.duoc.msvc.producto.exceptions.ProductoException;
import com.duoc.msvc.producto.models.entities.Producto;
import com.duoc.msvc.producto.repositories.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService{
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ProductoDTOModelAssembler assembler;

    @Override
    public List<ProductoDTO> findAllSimple() {
        return this.productoRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    @Override
    public List<ProductoDTO> findByCategoriaSimple(String categoria) {
        return this.productoRepository.findByCategoria(categoria).stream().map(this::convertToDTO).toList();
    }

    @Override
    public List<ProductoDTO> findByMarcaSimple(String marca) {
        return this.productoRepository.findByMarca(marca).stream().map(this::convertToDTO).toList();
    }

    @Override
    public CollectionModel<ProductoHateoasDTO> findAllHateoas() {
        List<Producto> productos = this.productoRepository.findAll();
        return assembler.toCollectionModel(productos);
    }

    @Override
    public CollectionModel<ProductoHateoasDTO> findByCategoria(String categoria) {
        List<Producto> productos = this.productoRepository.findByCategoria(categoria);
        return assembler.toCollectionModel(productos);
    }

    @Override
    public CollectionModel<ProductoHateoasDTO> findByMarca(String marca) {
        List<Producto> productos = this.productoRepository.findByMarca(marca);
        return assembler.toCollectionModel(productos);
    }

    @Override
    public ProductoHateoasDTO findById(Long id) {
        Producto producto = this.productoRepository.findById(id).orElseThrow(
                () -> new ProductoException("El producto con el id " + id + " no existe")
        );
        return assembler.toModel(producto);
    }

    @Transactional
    @Override
    public ProductoHateoasDTO save(Producto producto) {
        Producto savedProducto = this.productoRepository.save(producto);
        return assembler.toModel(savedProducto);
    }

    @Transactional
    @Override
    public ProductoHateoasDTO updateById(Long id, Producto producto) {
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
        Producto updatedProducto = productoRepository.save(productoDb);
        return assembler.toModel(updatedProducto);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        productoRepository.findById(id).orElseThrow(
            () -> new ProductoException("El producto con el id " + id + " no existe en la base de datos")
        );
        productoRepository.deleteById(id);
    }

    private ProductoDTO convertToDTO(Producto producto) {
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
