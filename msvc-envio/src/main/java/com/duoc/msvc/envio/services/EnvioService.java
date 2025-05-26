package com.duoc.msvc.envio.services;

import com.duoc.msvc.envio.dtos.EnvioDTO;
import com.duoc.msvc.envio.models.entities.Envio;

import java.math.BigDecimal;
import java.util.List;

public interface EnvioService {
    List<EnvioDTO> findAll();
    EnvioDTO findById(Long id);
    EnvioDTO save(Envio envio);
    BigDecimal getCostoEnvio();
    EnvioDTO convertToDTO(Envio envio);

}
