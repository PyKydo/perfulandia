package com.duoc.msvc.sucursal.services;

import com.duoc.msvc.sucursal.dtos.SucursalDTO;
import com.duoc.msvc.sucursal.models.entities.Sucursal;

import java.util.List;

public interface SucursalService {
    List<SucursalDTO> findAll();
    SucursalDTO findById(Long id);
    SucursalDTO save(Sucursal sucursal);
    SucursalDTO convertToDTO(Sucursal sucursal);
}
