package com.duoc.msvc.envio.services;

import com.duoc.msvc.envio.clients.PedidoClient;
import com.duoc.msvc.envio.dtos.EnvioDTO;
import com.duoc.msvc.envio.exceptions.EnvioException;
import com.duoc.msvc.envio.models.entities.Envio;
import com.duoc.msvc.envio.repositories.EnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

@Service
public class EnvioServicelmpl implements EnvioService{

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private PedidoClient pedidoClient;

    @Override
    public List<EnvioDTO> findAll() {
        return this.envioRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public EnvioDTO findById(Long id) {
        Envio envio = this.envioRepository.findById(id).orElseThrow(
                () -> new EnvioException("El envio con id "+id+" no se encuentra en la base de datos")
        );
        return convertToDTO(envio);
    }

    @Override
    public EnvioDTO save(Envio envio) {
        if(envio.getCosto() == null){
            envio.setCosto(getCostoEnvio());
        }

        envio.setEstado("Pendiente");

        return convertToDTO(this.envioRepository.save(envio));
    }

    @Override
    public EnvioDTO convertToDTO(Envio envio) {
        EnvioDTO dto = new EnvioDTO();
        dto.setCosto(envio.getCosto());
        dto.setCiudad(envio.getCiudad());
        dto.setDireccion(envio.getDireccion());
        dto.setComuna(envio.getComuna());
        dto.setEstado(envio.getEstado());
        dto.setRegion(envio.getRegion());
        dto.setCodigoPostal(envio.getCodigoPostal());
        dto.setIdPedido(envio.getIdPedido());

        return dto;
    }

    public BigDecimal getCostoEnvio() {
        Random random = new Random();

        int distanciaKm = random.nextInt(96) + 5;

        double pesoRandom = 0.5 + (20.0 - 0.5) * random.nextDouble();
        BigDecimal pesoKg = BigDecimal.valueOf(pesoRandom).setScale(2, RoundingMode.HALF_UP);

        BigDecimal base = new BigDecimal("2000");
        BigDecimal tarifaPorKm = new BigDecimal("60");
        BigDecimal tarifaPorKg = new BigDecimal("300");

        BigDecimal costoDistancia = tarifaPorKm.multiply(BigDecimal.valueOf(distanciaKm));
        BigDecimal costoPeso = tarifaPorKg.multiply(pesoKg);
        BigDecimal costoTotal = base.add(costoDistancia).add(costoPeso);

        costoTotal = costoTotal.setScale(0, RoundingMode.HALF_UP);

        return costoTotal;
    }

    @Override
    public String updateEstadoById(Long idPedido, String nuevoEstado) {
        Envio envio = this.envioRepository.findByIdPedido(idPedido);
        if (envio == null) {
            throw new EnvioException("No existe un envío para el pedido con id " + idPedido);
        }

        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            throw new EnvioException("El nuevo estado no puede estar vacío");
        }

        String estadoActual = envio.getEstado();
        try {
            envio.setEstado(nuevoEstado);
            envioRepository.save(envio);

            // Si el estado es "Enviado", actualizamos el pedido
            if ("Enviado".equalsIgnoreCase(nuevoEstado)) {
                try {
                    String resultado = pedidoClient.updateEstadoById(idPedido, "Entregado");
                    if (resultado == null || resultado.trim().isEmpty()) {
                        throw new EnvioException("No se recibió respuesta del servicio de pedidos");
                    }
                } catch (Exception e) {
                    // Si falla la actualización del pedido, revertimos el estado del envío
                    envio.setEstado(estadoActual);
                    envioRepository.save(envio);
                    throw new EnvioException("Error al actualizar el estado del pedido: " + e.getMessage());
                }
            }

            return envio.getEstado();
        } catch (Exception e) {
            // Si ocurre cualquier otro error, revertimos el estado del envío
            envio.setEstado(estadoActual);
            envioRepository.save(envio);
            throw new EnvioException("Error al actualizar el estado: " + e.getMessage());
        }
    }
}
