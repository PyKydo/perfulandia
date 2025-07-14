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
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import com.duoc.msvc.sucursal.controllers.SucursalHateoasController;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

@Service
public class SucursalServicelmpl implements SucursalService{
    private static final Logger logger = LoggerFactory.getLogger(SucursalServicelmpl.class);
    @Autowired
    private SucursalRepository sucursalRepository;
    @Autowired
    private ProductoClient productoClient;
    @Value("${msvc.producto.url}")
    private String productoUrl;
    @Value("${msvc.pedido.url}")
    private String pedidoUrl;

    @Override
    public List<SucursalGetDTO> findAll() {
        logger.info("Iniciando búsqueda de todas las sucursales");
        List<SucursalGetDTO> sucursales = this.sucursalRepository.findAll().stream()
                .map(this::convertToGetDTO)
                .toList();
        logger.info("Búsqueda completada. Se encontraron {} sucursales", sucursales.size());
        return sucursales;
    }

    @Override
    public SucursalGetDTO findById(Long id) {
        logger.info("Buscando sucursal con ID: {}", id);
        Sucursal sucursal = this.sucursalRepository.findById(id).orElseThrow(
                () -> new SucursalException("La sucursal con id " + id + " no existe")
        );
        logger.info("Sucursal encontrada: {} - {}", sucursal.getDireccion(), sucursal.getRegion());
        return convertToGetDTO(sucursal);
    }

    @Transactional
    @Override
    public SucursalGetDTO save(SucursalCreateDTO sucursalCreateDTO) {
        logger.info("Iniciando creación de sucursal: {} - {}", sucursalCreateDTO.getDireccion(), sucursalCreateDTO.getRegion());
        
        Sucursal sucursal = new Sucursal();
        sucursal.setDireccion(sucursalCreateDTO.getDireccion());
        sucursal.setRegion(sucursalCreateDTO.getRegion());
        sucursal.setComuna(sucursalCreateDTO.getComuna());
        sucursal.setCantidadPersonal(sucursalCreateDTO.getCantidadPersonal());
        sucursal.setHorariosAtencion(sucursalCreateDTO.getHorariosAtencion());
        
        logger.info("Creando inventarios para la nueva sucursal");
        List<Inventario> inventarios = new ArrayList<>();
        for (long j = 1L; j <= 100L; j++) {
            Inventario inventario = new Inventario();
            inventario.setSucursal(sucursal);
            inventario.setIdProducto(j);
            inventario.setStock(0);
            inventarios.add(inventario);
        }
        sucursal.setInventarios(inventarios);
        
        Sucursal savedSucursal = this.sucursalRepository.save(sucursal);
        logger.info("Sucursal creada exitosamente con ID: {} e {} inventarios inicializados", 
                   savedSucursal.getIdSucursal(), 
                   savedSucursal.getInventarios().size());
        
        return convertToGetDTO(savedSucursal);
    }

    @Transactional
    @Override
    public SucursalGetDTO updateById(Long id, SucursalUpdateDTO sucursalUpdateDTO) {
        logger.info("Iniciando actualización de sucursal con ID: {}", id);
        
        Sucursal sucursalDb = sucursalRepository.findById(id)
                .orElseThrow(() -> new SucursalException("Sucursal no encontrada con id: " + id));
        
        logger.info("Actualizando datos de sucursal - Dirección: {} -> {}, Región: {} -> {}", 
                   sucursalDb.getDireccion(), sucursalUpdateDTO.getDireccion(),
                   sucursalDb.getRegion(), sucursalUpdateDTO.getRegion());
        
        sucursalDb.setDireccion(sucursalUpdateDTO.getDireccion());
        sucursalDb.setRegion(sucursalUpdateDTO.getRegion());
        sucursalDb.setComuna(sucursalUpdateDTO.getComuna());
        sucursalDb.setCantidadPersonal(sucursalUpdateDTO.getCantidadPersonal());
        sucursalDb.setHorariosAtencion(sucursalUpdateDTO.getHorariosAtencion());
        
        Sucursal sucursalActualizada = sucursalRepository.save(sucursalDb);
        logger.info("Sucursal actualizada exitosamente");
        return convertToGetDTO(sucursalActualizada);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        logger.info("Iniciando eliminación de sucursal con ID: {}", id);
        
        sucursalRepository.findById(id).orElseThrow(
            () -> new SucursalException("Sucursal no encontrada con id: " + id)
        );
        
        sucursalRepository.deleteById(id);
        logger.info("Sucursal eliminada exitosamente");
    }

    @Override
    public SucursalGetDTO findByBestStock(Long idProducto) {
        logger.info("Buscando sucursal con mejor stock para producto ID: {}", idProducto);
        
        Long countInventarios = sucursalRepository.countInventariosByProducto(idProducto);
        if (countInventarios == 0) {
            logger.error("Error: El producto con ID {} no existe en ningún inventario de sucursal", idProducto);
            throw new SucursalException("El producto con ID " + idProducto + " no existe en ningún inventario de sucursal. Verifique que el producto esté registrado en el catálogo.");
        }
        
        Long countInventariosConStock = sucursalRepository.countInventariosWithStockByProducto(idProducto);
        if (countInventariosConStock == 0) {
            logger.error("Error: El producto con ID {} no tiene stock disponible en ninguna sucursal", idProducto);
            throw new SucursalException("El producto con ID " + idProducto + " existe en inventarios pero no tiene stock disponible en ninguna sucursal. Consulte disponibilidad más tarde.");
        }
        
        Sucursal sucursal = sucursalRepository.findBySucursalBestStock(idProducto);
        if (sucursal == null) {
            logger.error("Error interno: No se pudo encontrar la sucursal con mejor stock para el producto con ID {}", idProducto);
            throw new SucursalException("Error interno: No se pudo encontrar la sucursal con mejor stock para el producto con ID " + idProducto);
        }
        
        logger.info("Sucursal con mejor stock encontrada: {} - ID: {}", sucursal.getDireccion(), sucursal.getIdSucursal());
        return convertToGetDTO(sucursal);
    }

    @Transactional
    @Override
    public void updateStock(Long idSucursal, Long idInventario, Integer nuevoStock) {
        logger.info("Actualizando stock - Sucursal ID: {}, Inventario ID: {}, Nuevo stock: {}", idSucursal, idInventario, nuevoStock);
        
        Sucursal sucursal = sucursalRepository.findById(idSucursal)
                .orElseThrow(() -> new SucursalException("Sucursal no encontrada con id: " + idSucursal));
        
        Inventario inventario = sucursal.getInventarios().stream()
                .filter(inv -> inv.getIdInventario().equals(idInventario))
                .findFirst()
                .orElseThrow(() -> new SucursalException("Inventario no encontrado con id: " + idInventario));
        
        Integer stockAnterior = inventario.getStock();
        inventario.setStock(nuevoStock);
        sucursalRepository.save(sucursal);
        
        logger.info("Stock actualizado exitosamente de {} a {}", stockAnterior, nuevoStock);
    }

    @Override
    public SucursalGetDTO convertToDTO(Sucursal sucursal) {
        return convertToGetDTO(sucursal);
    }

    private SucursalGetDTO convertToGetDTO(Sucursal sucursal) {
        if (sucursal == null) {
            throw new SucursalException("La sucursal no puede ser null");
        }
        
        SucursalGetDTO dto = new SucursalGetDTO();
        dto.setId(sucursal.getIdSucursal());
        dto.setDireccion(sucursal.getDireccion());
        dto.setRegion(sucursal.getRegion());
        dto.setComuna(sucursal.getComuna());
        dto.setCantidadPersonal(sucursal.getCantidadPersonal());
        dto.setHorariosAtencion(sucursal.getHorariosAtencion());
        if (sucursal.getInventarios() == null) {
            logger.warn("Advertencia: Sucursal (id={}) - Inventarios es null", sucursal.getIdSucursal());
            dto.setInventarios(new ArrayList<>());
            return dto;
        }
        
        logger.debug("Sucursal (id={}) - Inventarios cargados: {}", 
                    sucursal.getIdSucursal(), 
                    sucursal.getInventarios().size());
        List<InventarioDTO> inventariosDTO = sucursal.getInventarios().stream().map(inv -> {
            InventarioDTO inventarioDTO = new InventarioDTO();
            inventarioDTO.setId(inv.getIdInventario());
            inventarioDTO.setIdProducto(inv.getIdProducto());
            inventarioDTO.setStock(inv.getStock());
            
            logger.debug("Obteniendo información del producto ID: {}", inv.getIdProducto());
            try {
                ProductoDTO producto = productoClient.findById(inv.getIdProducto());
                if (producto != null && producto.getNombre() != null && !producto.getNombre().trim().isEmpty()) {
                    inventarioDTO.setNombreProducto(producto.getNombre());
                    inventarioDTO.setMarcaProducto(producto.getMarca() != null ? producto.getMarca() : "Sin marca");
                    logger.debug("Producto ID {} - Nombre: {}, Marca: {}", inv.getIdProducto(), producto.getNombre(), producto.getMarca());
                } else {
                    logger.warn("Producto ID {} - Respuesta vacía o nula del microservicio de productos", inv.getIdProducto());
                    inventarioDTO.setNombreProducto("Producto no encontrado");
                    inventarioDTO.setMarcaProducto("Sin marca");
                }
            } catch (Exception e) {
                logger.error("Error al obtener información del producto ID {}: {}", inv.getIdProducto(), e.getMessage());
                logger.debug("Detalles del error para producto ID {}: {}", inv.getIdProducto(), e);
                inventarioDTO.setNombreProducto("Error de conexión");
                inventarioDTO.setMarcaProducto("No disponible");
            }
            return inventarioDTO;
        }).toList();
        
        dto.setInventarios(inventariosDTO);
        int totalStock = inventariosDTO.stream()
            .mapToInt(InventarioDTO::getStock)
            .sum();
        logger.debug("Sucursal (id={}) - Total stock: {}", sucursal.getIdSucursal(), totalStock);
        
        return dto;
    }
    @Override
    public CollectionModel<EntityModel<Sucursal>> findAllHateoas() {
        List<Sucursal> sucursales = this.sucursalRepository.findAll();
        List<EntityModel<Sucursal>> entityModels = sucursales.stream()
            .map(this::toEntityModel)
            .toList();
        return CollectionModel.of(entityModels,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SucursalHateoasController.class).findAll()).withSelfRel(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SucursalHateoasController.class).save(null)).withRel("create")
        );
    }

    @Override
    public EntityModel<Sucursal> findByIdHateoas(Long id) {
        Sucursal sucursal = this.sucursalRepository.findById(id).orElseThrow(
                () -> new SucursalException("La sucursal con id " + id + " no existe")
        );
        return toEntityModel(sucursal);
    }

    private EntityModel<Sucursal> toEntityModel(Sucursal sucursal) {
        if (sucursal == null) {
            throw new SucursalException("La sucursal no puede ser null");
        }
        EntityModel<Sucursal> entityModel = EntityModel.of(sucursal,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SucursalHateoasController.class).findById(sucursal.getIdSucursal())).withSelfRel(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SucursalHateoasController.class).findAll()).withRel("sucursales")
        );
        if (sucursal.getInventarios() != null) {
        }
        return entityModel;
    }

    @Override
    public String getProductoDisponibilidad(Long idProducto) {
        Long countInventarios = sucursalRepository.countInventariosByProducto(idProducto);
        if (countInventarios == 0) {
            return "El producto con ID " + idProducto + " no está registrado en ningún inventario de sucursal.";
        }
        
        Long countInventariosConStock = sucursalRepository.countInventariosWithStockByProducto(idProducto);
        if (countInventariosConStock == 0) {
            return "El producto con ID " + idProducto + " no tiene stock disponible en ninguna sucursal.";
        }
        
        return "El producto con ID " + idProducto + " tiene stock disponible en " + countInventariosConStock + " sucursal(es).";
    }
}
