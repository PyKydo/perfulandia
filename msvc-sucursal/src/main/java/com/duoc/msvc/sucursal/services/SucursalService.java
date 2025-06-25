package com.duoc.msvc.sucursal.services;

import com.duoc.msvc.sucursal.dtos.SucursalGetDTO;
import com.duoc.msvc.sucursal.dtos.SucursalCreateDTO;
import com.duoc.msvc.sucursal.dtos.SucursalUpdateDTO;
import com.duoc.msvc.sucursal.dtos.SucursalHateoasDTO;
import com.duoc.msvc.sucursal.models.entities.Sucursal;
import org.springframework.hateoas.CollectionModel;

import java.util.List;

public interface SucursalService {
    List<SucursalGetDTO> findAll();
    SucursalGetDTO findById(Long id);
    SucursalGetDTO save(SucursalCreateDTO sucursalCreateDTO);
    SucursalGetDTO updateById(Long id, SucursalUpdateDTO sucursalUpdateDTO);
    void deleteById(Long id);
    SucursalGetDTO findByBestStock(Long idProducto);
    void updateStock(Long idInventario, Long idSucursal, Integer nuevoStock);
    SucursalGetDTO convertToDTO(Sucursal sucursal);
    CollectionModel<SucursalHateoasDTO> findAllHateoas();
    SucursalHateoasDTO findByIdHateoas(Long id);
}
