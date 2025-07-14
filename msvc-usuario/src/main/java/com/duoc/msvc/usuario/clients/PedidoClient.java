package com.duoc.msvc.usuario.clients;

import com.duoc.msvc.usuario.dtos.pojos.PedidoClientDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-pedido", url = "http://localhost:8004/api/v1/pedidos")
public interface PedidoClient {
    @PostMapping("/cliente")
    PedidoClientDTO save(@Valid @RequestBody PedidoClientDTO pedidoClientDTO);

    @GetMapping("/cliente/{idCliente}/client")
    List<PedidoClientDTO> findAllByIdCliente(@PathVariable Long idCliente);

    @GetMapping("/{idPedido}/client")
    PedidoClientDTO findByIdPedido(@PathVariable Long idPedido);

    @PutMapping("/{idPedido}/actualizarEstado/{nuevoEstado}")
    String updateEstadoByIdPedido(@PathVariable Long idPedido, @PathVariable String nuevoEstado);

    @PutMapping("/{idCliente}/pagarPedido/{idPedido}")
    PedidoClientDTO pagarPedido(@PathVariable Long idCliente, @PathVariable Long idPedido);
}
