package com.duoc.msvc.pedido.clients;

import com.duoc.msvc.pedido.dtos.pojos.PagoClientDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "msvc-pago", url = "http://localhost:8005/api/v1/pagos")
public interface PagoClient {
    @PostMapping
    PagoClientDTO save(@Valid @RequestBody PagoClientDTO pagoClientDTO);

    @PutMapping("/actualizarEstado/{idPedido}/{nuevoEstado}")
    String updateEstadoById(@PathVariable Long idPedido, @PathVariable String nuevoEstado);
}
