package com.duoc.msvc.producto.services;

import com.duoc.msvc.producto.exceptions.ProductoException;
import com.duoc.msvc.producto.models.entities.Producto;
import com.duoc.msvc.producto.repositories.ProductoRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProductoServiceImpl implements ProductoService{
    
    private static final Logger logger = LoggerFactory.getLogger(ProductoServiceImpl.class);
    
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public CollectionModel<EntityModel<Producto>> findAll() {
        logger.info("Iniciando búsqueda de todos los productos");
        List<Producto> productos = this.productoRepository.findAll();
        List<EntityModel<Producto>> productoModels = productos.stream().map(this::toEntityModel).toList();
        CollectionModel<EntityModel<Producto>> collection = CollectionModel.of(productoModels);
        collection.add(linkTo(methodOn(com.duoc.msvc.producto.controllers.ProductoHATEOASController.class).findAll()).withSelfRel());
        logger.info("Búsqueda completada. Se encontraron {} productos", productos.size());
        return collection;
    }

    @Override
    public CollectionModel<EntityModel<Producto>> findByCategoria(String categoria) {
        logger.info("Buscando productos por categoría: {}", categoria);
        List<Producto> productos = this.productoRepository.findByCategoria(categoria);
        List<EntityModel<Producto>> productoModels = productos.stream().map(this::toEntityModel).toList();
        CollectionModel<EntityModel<Producto>> collection = CollectionModel.of(productoModels);
        collection.add(linkTo(methodOn(com.duoc.msvc.producto.controllers.ProductoHATEOASController.class).findByCategoria(categoria)).withSelfRel());
        logger.info("Se encontraron {} productos en la categoría: {}", productos.size(), categoria);
        return collection;
    }

    @Override
    public CollectionModel<EntityModel<Producto>> findByMarca(String marca) {
        logger.info("Buscando productos por marca: {}", marca);
        List<Producto> productos = this.productoRepository.findByMarca(marca);
        List<EntityModel<Producto>> productoModels = productos.stream().map(this::toEntityModel).toList();
        CollectionModel<EntityModel<Producto>> collection = CollectionModel.of(productoModels);
        collection.add(linkTo(methodOn(com.duoc.msvc.producto.controllers.ProductoHATEOASController.class).findByMarca(marca)).withSelfRel());
        logger.info("Se encontraron {} productos de la marca: {}", productos.size(), marca);
        return collection;
    }

    @Override
    public EntityModel<Producto> findById(Long id) {
        logger.info("Buscando producto con ID: {}", id);
        Producto producto = this.productoRepository.findById(id).orElseThrow(
                () -> new ProductoException("El producto con el id " + id + " no existe")
        );
        logger.info("Producto encontrado: {} - {}", producto.getNombre(), producto.getMarca());
        return toEntityModel(producto);
    }

    @Transactional
    @Override
    public EntityModel<Producto> save(Producto producto) {
        logger.info("Iniciando creación de producto: {} - {}", producto.getNombre(), producto.getMarca());
        Producto savedProducto = this.productoRepository.save(producto);
        logger.info("Producto creado exitosamente con ID: {}", savedProducto.getIdProducto());
        return toEntityModel(savedProducto);
    }

    @Transactional
    @Override
    public EntityModel<Producto> updateById(Long id, Producto producto) {
        logger.info("Iniciando actualización de producto con ID: {}", id);
        Producto productoDb = productoRepository.findById(id).orElseThrow(
            () -> new ProductoException("El producto con el id " + id + " no existe en la base de datos")
        );
        
        logger.info("Actualizando datos del producto - Nombre: {} -> {}, Marca: {} -> {}", 
                   productoDb.getNombre(), producto.getNombre(),
                   productoDb.getMarca(), producto.getMarca());
        
        productoDb.setNombre(producto.getNombre());
        productoDb.setMarca(producto.getMarca());
        productoDb.setPrecio(producto.getPrecio());
        productoDb.setDescripcion(producto.getDescripcion());
        productoDb.setImagenRepresentativaURL(producto.getImagenRepresentativaURL());
        productoDb.setCategoria(producto.getCategoria());
        productoDb.setPorcentajeConcentracion(producto.getPorcentajeConcentracion());
        
        Producto updatedProducto = productoRepository.save(productoDb);
        logger.info("Producto actualizado exitosamente");
        return toEntityModel(updatedProducto);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        logger.info("Iniciando eliminación de producto con ID: {}", id);
        productoRepository.findById(id).orElseThrow(
            () -> new ProductoException("El producto con el id " + id + " no existe en la base de datos")
        );
        productoRepository.deleteById(id);
        logger.info("Producto eliminado exitosamente");
    }

    private EntityModel<Producto> toEntityModel(Producto producto) {
        return EntityModel.of(producto,
            linkTo(methodOn(com.duoc.msvc.producto.controllers.ProductoHATEOASController.class).findById(producto.getIdProducto())).withSelfRel(),
            linkTo(methodOn(com.duoc.msvc.producto.controllers.ProductoHATEOASController.class).findAll()).withRel("productos"),
            linkTo(methodOn(com.duoc.msvc.producto.controllers.ProductoHATEOASController.class).findByCategoria(producto.getCategoria())).withRel("productosPorCategoria"),
            linkTo(methodOn(com.duoc.msvc.producto.controllers.ProductoHATEOASController.class).findByMarca(producto.getMarca())).withRel("productosPorMarca")
        );
    }
}
