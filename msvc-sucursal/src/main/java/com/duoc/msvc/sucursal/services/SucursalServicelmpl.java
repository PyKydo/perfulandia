package com.duoc.msvc.sucursal.services;

import com.duoc.msvc.sucursal.dtos.InventarioDTO;
import com.duoc.msvc.sucursal.dtos.SucursalDTO;
import com.duoc.msvc.sucursal.exceptions.SucursalException;
import com.duoc.msvc.sucursal.models.entities.Inventario;
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
    public SucursalDTO updateById(Long id, Sucursal sucursal) {
        Sucursal newSucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con id: " + id));

        newSucursal.setDireccion(sucursal.getDireccion());
        newSucursal.setRegion(sucursal.getRegion());
        newSucursal.setComuna(sucursal.getComuna());
        newSucursal.setCantidadPersonal(sucursal.getCantidadPersonal());
        newSucursal.setHorariosAtencion(sucursal.getHorariosAtencion());

        return convertToDTO(sucursalRepository.save(newSucursal));
    }

    @Override
    public void deleteById(Long id) {
        this.sucursalRepository.deleteById(id);
    }

    @Override
    public SucursalDTO findByBestStock(Long idProducto) {
        Sucursal sucursal = sucursalRepository.findBySucursalBestStock(idProducto);
        return convertToDTO(sucursal);
    }

    @Override
    public void updateStock(Long idSucursal, Long idInventario, Integer nuevoStock) {
        // Buscar la sucursal por su ID
        Sucursal sucursal = sucursalRepository.findById(idSucursal)
                .orElseThrow(() -> new SucursalException("Sucursal no encontrada"));

        // Buscar el inventario dentro de la sucursal
        Inventario inventario = sucursal.getInventarios().stream()
                .filter(inv -> inv.getIdInventario().equals(idInventario))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado en la sucursal"));

        // Actualizar el stock
        inventario.setStock(nuevoStock);

        // Guardar los cambios en la sucursal (cascade debería encargarse del inventario)
        sucursalRepository.save(sucursal);
    }

    @Override
    public SucursalDTO convertToDTO(Sucursal sucursal) {
        SucursalDTO dto = new SucursalDTO();
        dto.setRegion(sucursal.getRegion());
        dto.setComuna(sucursal.getComuna());
        dto.setCantidadPersonal(sucursal.getCantidadPersonal());
        dto.setDireccion(sucursal.getDireccion());
        dto.setHorariosAtencion(sucursal.getHorariosAtencion());
        dto.setIdSucursal(sucursal.getIdSucursal());

        List<InventarioDTO> inventariosDTO = sucursal.getInventarios().stream()
                .map(inventario -> {
                    InventarioDTO inventarioDTO = new InventarioDTO();
                    inventarioDTO.setIdInventario(inventario.getIdInventario());
                    inventarioDTO.setStock(inventario.getStock());
                    inventarioDTO.setIdProducto(inventario.getIdProducto());


                    return inventarioDTO;
                }
                ).toList();

        dto.setInventarios(inventariosDTO);
        return dto;
    }


}
