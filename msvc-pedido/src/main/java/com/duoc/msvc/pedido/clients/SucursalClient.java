package com.duoc.msvc.pedido.clients;

import com.duoc.msvc.pedido.dtos.pojos.ProductoClientDTO;
import com.duoc.msvc.pedido.dtos.pojos.SucursalClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "msvc-sucursal", url = "${msvc.sucursal.url}")
public interface SucursalClient {
    @GetMapping("/{id}")
    SucursalClientDTO getSucursalById(@PathVariable Long id);

    @GetMapping("/best-stock/{idProducto}")
    SucursalClientDTO getSucursalBestStockByIdProducto(@PathVariable Long idProducto);

    @PutMapping("/{idSucursal}/inventario/{idInventario}/stock")
    void updateStock(@PathVariable Long idSucursal,
                     @PathVariable Long idInventario,
                     @RequestParam Integer nuevoStock);
}
