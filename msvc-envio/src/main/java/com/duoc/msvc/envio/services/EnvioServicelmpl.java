package com.duoc.msvc.envio.services;

import com.duoc.msvc.envio.exceptions.EnvioException;
import com.duoc.msvc.envio.models.entities.Envio;
import com.duoc.msvc.envio.repositories.EnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvioServicelmpl implements EnvioService{

    @Autowired
    private EnvioRepository envioRepository;


    @Override
    public List<Envio> findAll() {
        return this.envioRepository.findAll();
    }

    @Override
    public Envio findById(Long id) {
        return this.envioRepository.findById(id).orElseThrow(
                () -> new EnvioException("El envio con id "+id+" no se encuentra en la base de datos")
        );
    }

    @Override
    public Envio save(Envio envio) {
        return this.envioRepository.save(envio);
    }
}
