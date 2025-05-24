package com.duoc.msvc.sucursal.services;

import com.duoc.msvc.sucursal.models.Sucursal;

import java.util.List;

public interface SucursalService {

    List<Sucursal> findAll();
    Sucursal findById(Long id);
    Sucursal save(Sucursal sucursal);
}
