package com.duoc.msvc.sucursal.services;

import com.duoc.msvc.sucursal.exceptions.SucursalException;
import com.duoc.msvc.sucursal.models.Sucursal;
import com.duoc.msvc.sucursal.repositories.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalServicelmpl implements SucursalService{

    @Autowired
    private SucursalRepository sucursalRepository;


    @Override
    public List<Sucursal> findAll() {
        return this.sucursalRepository.findAll();
    }

    @Override
    public Sucursal findById(Long id) {
        return this.sucursalRepository.findById(id).orElseThrow(
                () -> new SucursalException("El envio con id "+id+" no se encuentra en la base de datos")
        );
    }

    @Override
    public Sucursal save(Sucursal sucursal) {
        return this.sucursalRepository.save(sucursal);
    }


}
