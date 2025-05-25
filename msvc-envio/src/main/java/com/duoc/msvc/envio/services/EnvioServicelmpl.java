package com.duoc.msvc.envio.services;

import com.duoc.msvc.envio.dtos.EnvioDTO;
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
        return convertToDTO(this.envioRepository.save(envio));
    }

    @Override
    public EnvioDTO convertToDTO(Envio envio) {
        EnvioDTO dto = new EnvioDTO();
        dto.setCiudad(envio.getCiudad());
        dto.setDireccion(envio.getDireccion());
        dto.setComuna(envio.getComuna());
        dto.setEstado(envio.getEstado());
        dto.setRegion(envio.getRegion());
        dto.setCodigoPostal(envio.getCodigoPostal());

        return dto;
    }
}
