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

    @Override
    public SucursalDTO save(Sucursal sucursal) {
        return convertToDTO(this.sucursalRepository.save(sucursal));
    }

    @Override
    public SucursalDTO updateById(Long id, Sucursal sucursalActualizada) {
        Sucursal sucursalExistente = sucursalRepository.findById(id)
                .orElseThrow(() -> new SucursalException("La sucursal con id " + id + " no existe"));

        sucursalActualizada.setIdSucursal(sucursalExistente.getIdSucursal());

        List<Inventario> inventariosFinales = new ArrayList<>();

        if (sucursalActualizada.getInventarios() != null) {
            for (Inventario nuevoInv : sucursalActualizada.getInventarios()) {
                Optional<Inventario> existenteOpt = sucursalExistente.getInventarios().stream()
                        .filter(i -> i.getIdProducto().equals(nuevoInv.getIdProducto()))
                        .findFirst();

                if (existenteOpt.isPresent()) {
                    Inventario existente = existenteOpt.get();
                    existente.setStock(nuevoInv.getStock());
                    inventariosFinales.add(existente);
                } else {
                    nuevoInv.setSucursal(sucursalActualizada); // relación bidireccional
                    inventariosFinales.add(nuevoInv);
                }
            }
        }

        sucursalActualizada.setInventarios(inventariosFinales);

        return convertToDTO(sucursalRepository.save(sucursalActualizada));
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
        Sucursal sucursal = sucursalRepository.findById(idSucursal)
                .orElseThrow(() -> new SucursalException("La sucursal con id " + idSucursal + " no existe"));

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
        dto.setRegion(sucursal.getRegion());
        dto.setComuna(sucursal.getComuna());
        dto.setCantidadPersonal(sucursal.getCantidadPersonal());
        dto.setDireccion(sucursal.getDireccion());
        dto.setHorariosAtencion(sucursal.getHorariosAtencion());
        dto.setIdSucursal(sucursal.getIdSucursal());

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
