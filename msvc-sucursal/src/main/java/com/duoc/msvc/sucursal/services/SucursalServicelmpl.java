package com.duoc.msvc.sucursal.services;

import com.duoc.msvc.sucursal.dtos.InventarioDTO;
import com.duoc.msvc.sucursal.dtos.SucursalDTO;
import com.duoc.msvc.sucursal.exceptions.SucursalException;
import com.duoc.msvc.sucursal.models.entities.Sucursal;
import com.duoc.msvc.sucursal.repositories.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalServicelmpl implements SucursalService{
    @Autowired
    private SucursalRepository sucursalRepository;

    @Override
    public List<SucursalDTO> findAll() {
        return this.sucursalRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public SucursalDTO findById(Long id) {
        Sucursal sucursal = this.sucursalRepository.findById(id).orElseThrow(
                () -> new SucursalException("El envio con id "+id+" no se encuentra en la base de datos")
        );
        return convertToDTO(sucursal);
    }

    @Override
    public SucursalDTO save(Sucursal sucursal) {
        return convertToDTO(this.sucursalRepository.save(sucursal));
    }

    @Override
    public SucursalDTO convertToDTO(Sucursal sucursal) {
        SucursalDTO dto = new SucursalDTO();
        dto.setRegion(sucursal.getRegion());
        dto.setComuna(sucursal.getComuna());
        dto.setCantidadPersonal(sucursal.getCantidadPersonal());
        dto.setDireccion(sucursal.getDireccion());
        dto.setHorariosAtencion(sucursal.getHorariosAtencion());

        List<InventarioDTO> inventariosDTO = sucursal.getInventarios().stream()
                .map(inventario -> {
                    InventarioDTO inventarioDTO = new InventarioDTO();
                    inventarioDTO.setStock(inventario.getStock());
                    inventarioDTO.setIdProducto(inventario.getIdProducto());

                    return inventarioDTO;
                }
                ).toList();

        dto.setInventarios(inventariosDTO);
        return dto;
    }


}
