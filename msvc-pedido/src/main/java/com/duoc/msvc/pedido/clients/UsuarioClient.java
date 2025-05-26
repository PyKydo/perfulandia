package com.duoc.msvc.pedido.clients;

import com.duoc.msvc.pedido.dtos.pojos.UsuarioClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-usuario", url = "http://localhost:8001/api/v1/usuarios")
public interface UsuarioClient {
    @GetMapping("/{id}")
    UsuarioClientDTO getUsuarioById(@PathVariable Long id);
}
