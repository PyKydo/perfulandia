package com.duoc.msvc.envio.services;

import com.duoc.msvc.envio.dtos.EnvioDTO;
import com.duoc.msvc.envio.dtos.EnvioGetDTO;
import com.duoc.msvc.envio.dtos.EnvioHateoasDTO;
import com.duoc.msvc.envio.models.entities.Envio;
import org.springframework.hateoas.CollectionModel;

import java.math.BigDecimal;
import java.util.List;

public interface EnvioService {
    List<EnvioGetDTO> findAll();
    EnvioGetDTO findById(Long id);
    EnvioGetDTO save(Envio envio);
    EnvioGetDTO updateById(Long id, Envio envio);
    void deleteById(Long id);
    BigDecimal getCostoEnvio();
    String updateEstadoById(Long id, String nuevoEstado);
    EnvioGetDTO convertToDTO(Envio envio);
    CollectionModel<EnvioHateoasDTO> findAllHateoas();
    EnvioHateoasDTO findByIdHateoas(Long id);
}
