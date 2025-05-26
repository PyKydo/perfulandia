package com.duoc.msvc.pedido.clients;

import com.duoc.msvc.pedido.dtos.pojos.ProductoClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-producto", url = "http://localhost:8003/api/v1/productos")
public interface ProductoClient {
    @GetMapping("/{id}")
    ProductoClientDTO getProductoById(@PathVariable Long id);
}
