package com.duoc.msvc.producto.assemblers;

import com.duoc.msvc.producto.controllers.ProductoHATEOASController;
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
        productoDTO.add(linkTo(methodOn(ProductoHATEOASController.class).findById(producto.getIdProducto())).withSelfRel());
        productoDTO.add(linkTo(methodOn(ProductoHATEOASController.class).findAll()).withRel("productos"));
        productoDTO.add(linkTo(methodOn(ProductoHATEOASController.class).findByCategoria(producto.getCategoria())).withRel("productosPorCategoria"));
        productoDTO.add(linkTo(methodOn(ProductoHATEOASController.class).findByMarca(producto.getMarca())).withRel("productosPorMarca"));

        return productoDTO;
    }

    @Override
    public CollectionModel<ProductoHateoasDTO> toCollectionModel(Iterable<? extends Producto> entities) {
        CollectionModel<ProductoHateoasDTO> productoDTOs = RepresentationModelAssembler.super.toCollectionModel(entities);
        
        // Agregar enlace a la colección
        productoDTOs.add(linkTo(methodOn(ProductoHATEOASController.class).findAll()).withSelfRel());
        
        return productoDTOs;
    }
} 