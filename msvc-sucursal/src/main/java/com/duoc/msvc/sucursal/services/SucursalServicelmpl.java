package com.duoc.msvc.sucursal.services;

import com.duoc.msvc.sucursal.clients.ProductoClient;
import com.duoc.msvc.sucursal.dtos.*;
import com.duoc.msvc.sucursal.dtos.pojos.ProductoDTO;
import com.duoc.msvc.sucursal.exceptions.SucursalException;
import com.duoc.msvc.sucursal.models.entities.Inventario;
import com.duoc.msvc.sucursal.models.entities.Sucursal;
import com.duoc.msvc.sucursal.repositories.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SucursalServicelmpl implements SucursalService{
    @Autowired
    private SucursalRepository sucursalRepository;
    @Autowired
    private ProductoClient productoClient;

    @Override
    public List<SucursalGetDTO> findAll() {
        return this.sucursalRepository.findAll().stream()
                .map(this::convertToGetDTO)
                .toList();
    }

    @Override
    public SucursalGetDTO findById(Long id) {
        Sucursal sucursal = this.sucursalRepository.findById(id).orElseThrow(
                () -> new SucursalException("La sucursal con id " + id + " no existe")
        );
        return convertToGetDTO(sucursal);
    }

    @Transactional
    @Override
    public SucursalGetDTO save(SucursalCreateDTO sucursalCreateDTO) {
        Sucursal sucursal = new Sucursal();
        sucursal.setDireccion(sucursalCreateDTO.getDireccion());
        sucursal.setRegion(sucursalCreateDTO.getRegion());
        sucursal.setComuna(sucursalCreateDTO.getComuna());
        sucursal.setCantidadPersonal(sucursalCreateDTO.getCantidadPersonal());
        sucursal.setHorariosAtencion(sucursalCreateDTO.getHorariosAtencion());
        return convertToGetDTO(this.sucursalRepository.save(sucursal));
    }

    @Transactional
    @Override
    public SucursalGetDTO updateById(Long id, SucursalUpdateDTO sucursalUpdateDTO) {
        Sucursal sucursalDb = sucursalRepository.findById(id)
                .orElseThrow(() -> new SucursalException("Sucursal no encontrada con id: " + id));
        sucursalDb.setDireccion(sucursalUpdateDTO.getDireccion());
        sucursalDb.setRegion(sucursalUpdateDTO.getRegion());
        sucursalDb.setComuna(sucursalUpdateDTO.getComuna());
        sucursalDb.setCantidadPersonal(sucursalUpdateDTO.getCantidadPersonal());
        sucursalDb.setHorariosAtencion(sucursalUpdateDTO.getHorariosAtencion());
        return convertToGetDTO(sucursalRepository.save(sucursalDb));
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
    public SucursalGetDTO findByBestStock(Long idProducto) {
        Sucursal sucursal = sucursalRepository.findBySucursalBestStock(idProducto);
        return convertToGetDTO(sucursal);
    }

    @Transactional
    @Override
    public void updateStock(Long idSucursal, Long idInventario, Integer nuevoStock) {
        Sucursal sucursal = sucursalRepository.findById(idSucursal)
                .orElseThrow(() -> new SucursalException("Sucursal no encontrada con id: " + idSucursal));
        Inventario inventario = sucursal.getInventarios().stream()
                .filter(inv -> inv.getIdInventario().equals(idInventario))
                .findFirst()
                .orElseThrow(() -> new SucursalException("Inventario no encontrado con id: " + idInventario));
        inventario.setStock(nuevoStock);
        sucursalRepository.save(sucursal);
    }

    @Override
    public SucursalGetDTO convertToDTO(Sucursal sucursal) {
        return convertToGetDTO(sucursal);
    }

    private SucursalGetDTO convertToGetDTO(Sucursal sucursal) {
        SucursalGetDTO dto = new SucursalGetDTO();
        dto.setId(sucursal.getIdSucursal());
        dto.setDireccion(sucursal.getDireccion());
        dto.setRegion(sucursal.getRegion());
        dto.setComuna(sucursal.getComuna());
        dto.setCantidadPersonal(sucursal.getCantidadPersonal());
        dto.setHorariosAtencion(sucursal.getHorariosAtencion());
        // Mapear inventarios con nombre y marca del producto
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
        return dto;
    }

    // Métodos HATEOAS
    @Override
    public CollectionModel<SucursalHateoasDTO> findAllHateoas() {
        List<Sucursal> sucursales = this.sucursalRepository.findAll();
        List<SucursalHateoasDTO> dtos = sucursales.stream().map(this::convertToHateoasDTO).toList();
        return CollectionModel.of(dtos);
    }

    @Override
    public SucursalHateoasDTO findByIdHateoas(Long id) {
        Sucursal sucursal = this.sucursalRepository.findById(id).orElseThrow(
                () -> new SucursalException("La sucursal con id " + id + " no existe")
        );
        return convertToHateoasDTO(sucursal);
    }

    private SucursalHateoasDTO convertToHateoasDTO(Sucursal sucursal) {
        SucursalHateoasDTO dto = new SucursalHateoasDTO();
        dto.setId(sucursal.getIdSucursal());
        dto.setDireccion(sucursal.getDireccion());
        dto.setRegion(sucursal.getRegion());
        dto.setComuna(sucursal.getComuna());
        dto.setCantidadPersonal(sucursal.getCantidadPersonal());
        dto.setHorariosAtencion(sucursal.getHorariosAtencion());
        // Mapear inventarios con nombre y marca del producto
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
        return dto;
    }
}
