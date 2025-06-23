package com.duoc.msvc.envio.services;

import com.duoc.msvc.envio.dtos.EnvioDTO;
import com.duoc.msvc.envio.models.entities.Envio;

import java.math.BigDecimal;
import java.util.List;

public interface EnvioService {
    List<EnvioDTO> findAll();
    EnvioDTO findById(Long id);
    EnvioDTO save(Envio envio);
    EnvioDTO updateById(Long id, Envio envio);
    void deleteById(Long id);
    BigDecimal getCostoEnvio();
    String updateEstadoById(Long id, String nuevoEstado);
    EnvioDTO convertToDTO(Envio envio);
}
