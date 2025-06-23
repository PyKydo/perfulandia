package com.duoc.msvc.pedido.clients;

import com.duoc.msvc.pedido.dtos.pojos.UsuarioClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-usuario", url = "${msvc.usuario.url}")
public interface UsuarioClient {
    @GetMapping("/{id}")
    UsuarioClientDTO getUsuarioById(@PathVariable Long id);
}
