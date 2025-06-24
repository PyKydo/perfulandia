package com.duoc.msvc.producto.assemblers;

import com.duoc.msvc.producto.controllers.ProductoController;
import com.duoc.msvc.producto.dtos.ProductoHateoasDTO;
import com.duoc.msvc.producto.models.entities.Producto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductoDTOModelAssembler implements RepresentationModelAssembler<Producto, ProductoHateoasDTO> {

    @Override
    public ProductoHateoasDTO toModel(Producto producto) {
        ProductoHateoasDTO productoDTO = new ProductoHateoasDTO();
        productoDTO.setId(producto.getIdProducto());
        productoDTO.setNombre(producto.getNombre());
        productoDTO.setMarca(producto.getMarca());
        productoDTO.setPrecio(producto.getPrecio());
        productoDTO.setDescripcion(producto.getDescripcion());
        productoDTO.setImagenRepresentativaURL(producto.getImagenRepresentativaURL());
        productoDTO.setCategoria(producto.getCategoria());
        productoDTO.setPorcentajeConcentracion(producto.getPorcentajeConcentracion());

        // Agregar enlaces HATEOAS
        productoDTO.add(linkTo(methodOn(ProductoController.class).findById(producto.getIdProducto())).withSelfRel());
        productoDTO.add(linkTo(methodOn(ProductoController.class).findAllHateoas()).withRel("productos"));
        productoDTO.add(linkTo(methodOn(ProductoController.class).findByCategoria(producto.getCategoria())).withRel("productosPorCategoria"));
        productoDTO.add(linkTo(methodOn(ProductoController.class).findByMarca(producto.getMarca())).withRel("productosPorMarca"));

        return productoDTO;
    }

    @Override
    public CollectionModel<ProductoHateoasDTO> toCollectionModel(Iterable<? extends Producto> entities) {
        CollectionModel<ProductoHateoasDTO> productoDTOs = RepresentationModelAssembler.super.toCollectionModel(entities);
        
        // Agregar enlace a la colección
        productoDTOs.add(linkTo(methodOn(ProductoController.class).findAllHateoas()).withSelfRel());
        
        return productoDTOs;
    }
} 