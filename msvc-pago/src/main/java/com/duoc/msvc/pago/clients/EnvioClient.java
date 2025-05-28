package com.duoc.msvc.pago.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "msvc-envio", url = "http://localhost:8006/api/v1/envios")
public interface EnvioClient {
    @PutMapping("/actualizarEstado/{idPedido}/{nuevoEstado}")
    String updateEstadoById(@PathVariable Long idPedido, @PathVariable String nuevoEstado);
}
