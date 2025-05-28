package com.duoc.msvc.pago.clients;

import com.duoc.msvc.pago.dtos.pojos.PedidoClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-pedido", url = "http://localhost:8004/api/v1/pedidos")
public interface PedidoClient {
    @GetMapping("/{id}")
    ResponseEntity<PedidoClientDTO> findById(@PathVariable Long id);


}
