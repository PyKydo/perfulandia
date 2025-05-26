package com.duoc.msvc.pedido.clients;

import com.duoc.msvc.pedido.dtos.pojos.ProductoClientDTO;
import com.duoc.msvc.pedido.dtos.pojos.SucursalClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "msvc-sucursal", url = "http://localhost:8002/api/v1/sucursales")
public interface SucursalClient {
    @GetMapping("/{id}")
    SucursalClientDTO getSucursalById(@PathVariable Long id);

    @GetMapping("/mejor-stock/{idProducto}")
    SucursalClientDTO getSucursalBestStockByIdProducto(@PathVariable Long idProducto);

    @PutMapping("/{idSuc}/inventario/{idInv}/stock")
    void updateInventarioStock(@PathVariable("idSuc") Long idSucursal,
                               @PathVariable("idInv") Long idInventario,
                               @RequestParam("nuevoStock") Integer nuevoStock);
    
}
