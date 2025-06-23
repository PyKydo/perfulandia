package com.duoc.msvc.sucursal.services;

import com.duoc.msvc.sucursal.clients.ProductoClient;
import com.duoc.msvc.sucursal.dtos.InventarioDTO;
import com.duoc.msvc.sucursal.dtos.SucursalDTO;
import com.duoc.msvc.sucursal.dtos.pojos.ProductoDTO;
import com.duoc.msvc.sucursal.exceptions.SucursalException;
import com.duoc.msvc.sucursal.models.entities.Inventario;
import com.duoc.msvc.sucursal.models.entities.Sucursal;
import com.duoc.msvc.sucursal.repositories.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SucursalServicelmpl implements SucursalService{
    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private ProductoClient productoClient;

    @Override
    public List<SucursalDTO> findAll() {
        return this.sucursalRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public SucursalDTO findById(Long id) {
        Sucursal sucursal = this.sucursalRepository.findById(id).orElseThrow(
                () -> new SucursalException("La sucursal con id " + id + " no existe")
        );
        return convertToDTO(sucursal);
    }

    @Transactional
    @Override
    public SucursalDTO save(Sucursal sucursal) {
        return convertToDTO(this.sucursalRepository.save(sucursal));
    }

    @Transactional
    @Override
    public SucursalDTO updateById(Long id, Sucursal sucursal) {
        Sucursal sucursalDb = sucursalRepository.findById(id)
                .orElseThrow(() -> new SucursalException("Sucursal no encontrada con id: " + id));

        sucursalDb.setDireccion(sucursal.getDireccion());
        sucursalDb.setRegion(sucursal.getRegion());
        sucursalDb.setComuna(sucursal.getComuna());
        sucursalDb.setCantidadPersonal(sucursal.getCantidadPersonal());
        sucursalDb.setHorariosAtencion(sucursal.getHorariosAtencion());

        return convertToDTO(sucursalRepository.save(sucursalDb));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        sucursalRepository.findById(id).orElseThrow(
            () -> new SucursalException("Sucursal no encontrada con id: " + id)
        );
        sucursalRepository.deleteById(id);
    }

    @Override
    public SucursalDTO findByBestStock(Long idProducto) {
        Sucursal sucursal = sucursalRepository.findBySucursalBestStock(idProducto);
        return convertToDTO(sucursal);
    }

    @Transactional
    @Override
    public void updateStock(Long idSucursal, Long idInventario, Integer nuevoStock) {
        Sucursal sucursal = sucursalRepository.findById(idSucursal)
                .orElseThrow(() -> new SucursalException("Sucursal no encontrada con id: " + idSucursal));

        Inventario inventario = sucursal.getInventarios().stream()
                .filter(inv -> inv.getIdInventario().equals(idInventario))
                .findFirst()
                .orElseThrow(() -> new SucursalException("La sucursal con id " + idInventario + " no existe"));

        inventario.setStock(nuevoStock);

        sucursalRepository.save(sucursal);
    }

    @Override
    public SucursalDTO convertToDTO(Sucursal sucursal) {
        SucursalDTO dto = new SucursalDTO();
        dto.setId(sucursal.getIdSucursal());
        dto.setDireccion(sucursal.getDireccion());
        dto.setRegion(sucursal.getRegion());
        dto.setComuna(sucursal.getComuna());
        dto.setCantidadPersonal(sucursal.getCantidadPersonal());
        dto.setHorariosAtencion(sucursal.getHorariosAtencion());

        List<InventarioDTO> inventariosDTO = sucursal.getInventarios().stream()
                .map(inventario -> {
                    ProductoDTO producto = productoClient.findById(inventario.getIdProducto());
                    InventarioDTO inventarioDTO = new InventarioDTO();
                    inventarioDTO.setStock(inventario.getStock());
                    inventarioDTO.setIdProducto(inventario.getIdProducto());
                    inventarioDTO.setNombreProducto(producto.getNombre());
                    inventarioDTO.setMarcaProducto(producto.getMarca());

                    return inventarioDTO;
                }
                ).toList();

        dto.setInventarios(inventariosDTO);
        return dto;
    }


}
