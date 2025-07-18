package com.duoc.msvc.pedido.clients;

import com.duoc.msvc.pedido.dtos.pojos.EnvioClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@FeignClient(name = "msvc-envio", url = "http://localhost:8005/api/v1/envios")
public interface EnvioClient {
    @GetMapping("/{id}")
    EnvioClientDTO findById(@PathVariable Long id);

    @PostMapping
    EnvioClientDTO save(@RequestBody EnvioClientDTO envioClientDTO);

    @PutMapping("/actualizarEstado/{idPedido}/{nuevoEstado}")
    String updateEstadoById(@PathVariable Long idPedido, @PathVariable String nuevoEstado);

    @GetMapping("/costoEnvio")
    BigDecimal getCostoEnvio();
}
