package com.duoc.msvc.envio.services;

import com.duoc.msvc.envio.models.entities.Envio;

import java.util.List;

public interface EnvioService {

    List<Envio> findAll();
    Envio findById(Long id);
    Envio save(Envio envio);
}
