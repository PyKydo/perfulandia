package com.duoc.msvc.envio.services;

import com.duoc.msvc.envio.dtos.EnvioDTO;
import com.duoc.msvc.envio.models.entities.Envio;

import java.util.List;

public interface EnvioService {
    List<EnvioDTO> findAll();
    EnvioDTO findById(Long id);
    EnvioDTO save(Envio envio);
    EnvioDTO convertToDTO(Envio envio);

}
