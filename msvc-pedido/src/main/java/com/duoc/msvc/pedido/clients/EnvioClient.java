package com.duoc.msvc.pedido.clients;

import com.duoc.msvc.pedido.dtos.pojos.EnvioClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "msvc-envio", url = "http://localhost:8006/api/v1/envios")
public interface EnvioClient {
    @GetMapping("/{id}")
    EnvioClientDTO getById(@PathVariable Long id);

    // TODO: Que deberia obtener el GetMapping?
    @GetMapping
    BigDecimal getCostoEnvio();
}
