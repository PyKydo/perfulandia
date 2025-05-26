package com.duoc.msvc.envio.clients;

import com.duoc.msvc.envio.dtos.pojos.PedidoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-pedido", url = "http://localhost:8004/api/v1/pedidos")
public interface PedidoClient {
    @GetMapping("/{id}")
    PedidoDTO getProductoById(@PathVariable Long id);
}
