package com.duoc.msvc.pago.services;

import com.duoc.msvc.pago.dtos.PagoDTO;
import com.duoc.msvc.pago.exceptions.PagoException;
import com.duoc.msvc.pago.models.Pago;
import com.duoc.msvc.pago.repositories.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagoServiceImpl implements PagoService{
    @Autowired
    private PagoRepository pagoRepository;


    @Override
    public List<PagoDTO> findAll() {
        return this.pagoRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    @Override
    public List<PagoDTO> findByEstado(String estado) {
        return this.pagoRepository.findByEstado(estado).stream().map(this::convertToDTO).toList();
    }

    @Override
    public PagoDTO findById(Long id) {
        Pago pago = this.pagoRepository.findById(id).orElseThrow(
                () -> new PagoException("El pago con id " + id + " no se encuentra en la base de datos")
        );

        return convertToDTO(pago);
    }

    @Override
    public PagoDTO save(Pago pago) {
        return convertToDTO( this.pagoRepository.save(pago));
    }

    @Override
    public PagoDTO convertToDTO(Pago pago){
        PagoDTO dto = new PagoDTO();

        dto.setEstado(pago.getEstado());
        dto.setMetodo(pago.getMetodo());
        dto.setFecha(pago.getFecha());
        dto.setMonto(pago.getMonto());

        return dto;
    }
}
