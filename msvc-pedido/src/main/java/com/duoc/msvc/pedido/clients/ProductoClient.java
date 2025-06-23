package com.duoc.msvc.pedido.clients;

import com.duoc.msvc.pedido.dtos.pojos.ProductoClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-producto", url = "${msvc.producto.url}")
public interface ProductoClient {
    @GetMapping("/{id}")
    ProductoClientDTO findById(@PathVariable Long id);
}
