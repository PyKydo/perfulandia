package com.duoc.msvc.sucursal.assemblers;

import com.duoc.msvc.sucursal.controllers.SucursalHATEOASController;
import com.duoc.msvc.sucursal.dtos.SucursalHateoasDTO;
import com.duoc.msvc.sucursal.dtos.InventarioDTO;
import com.duoc.msvc.sucursal.dtos.pojos.ProductoDTO;
import com.duoc.msvc.sucursal.models.entities.Sucursal;
import com.duoc.msvc.sucursal.models.entities.Inventario;
import com.duoc.msvc.sucursal.clients.ProductoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class SucursalDTOModelAssembler implements RepresentationModelAssembler<Sucursal, SucursalHateoasDTO> {
    @Autowired
    private ProductoClient productoClient;

    @Override
    public SucursalHateoasDTO toModel(Sucursal sucursal) {
        SucursalHateoasDTO dto = new SucursalHateoasDTO();
        dto.setId(sucursal.getIdSucursal());
        dto.setDireccion(sucursal.getDireccion());
        dto.setRegion(sucursal.getRegion());
        dto.setComuna(sucursal.getComuna());
        dto.setCantidadPersonal(sucursal.getCantidadPersonal());
        dto.setHorariosAtencion(sucursal.getHorariosAtencion());
        // Mapear inventarios igual que en el DTO simple
        List<InventarioDTO> inventariosDTO = sucursal.getInventarios().stream().map(inv -> {
            InventarioDTO inventarioDTO = new InventarioDTO();
            inventarioDTO.setId(inv.getIdInventario());
            inventarioDTO.setIdProducto(inv.getIdProducto());
            inventarioDTO.setStock(inv.getStock());
            try {
                ProductoDTO producto = productoClient.findById(inv.getIdProducto());
                inventarioDTO.setNombreProducto(producto.getNombre());
                inventarioDTO.setMarcaProducto(producto.getMarca());
            } catch (Exception e) {
                inventarioDTO.setNombreProducto("Desconocido");
                inventarioDTO.setMarcaProducto("Desconocida");
            }
            return inventarioDTO;
        }).toList();
        dto.setInventarios(inventariosDTO);
        dto.add(linkTo(methodOn(SucursalHATEOASController.class).findById(sucursal.getIdSucursal())).withSelfRel());
        dto.add(linkTo(methodOn(SucursalHATEOASController.class).findAll()).withRel("sucursales"));
        return dto;
    }
    @Override
    public CollectionModel<SucursalHateoasDTO> toCollectionModel(Iterable<? extends Sucursal> entities) {
        CollectionModel<SucursalHateoasDTO> dtos = RepresentationModelAssembler.super.toCollectionModel(entities);
        dtos.add(linkTo(methodOn(SucursalHATEOASController.class).findAll()).withSelfRel());
        return dtos;
    }
} 