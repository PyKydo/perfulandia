package com.duoc.msvc.sucursal.clients;

import com.duoc.msvc.sucursal.dtos.pojos.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "msvc-producto", url = "http://localhost:8003/api/v1/productos")
public interface ProductoClient {
    @GetMapping("/{id}")
    ProductoDTO getProductoById(Long id);
}
