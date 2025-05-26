package com.duoc.msvc.envio.services;

import com.duoc.msvc.envio.dtos.EnvioDTO;
import com.duoc.msvc.envio.exceptions.EnvioException;
import com.duoc.msvc.envio.models.entities.Envio;
import com.duoc.msvc.envio.repositories.EnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

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

    public BigDecimal getCostoEnvio() {
        Random random = new Random();

        int distanciaKm = random.nextInt(96) + 5; // 5 a 100

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
}
