package com.duoc.msvc.pago.services;

import com.duoc.msvc.pago.dtos.PagoDTO;
import com.duoc.msvc.pago.models.Pago;

import java.util.List;

public interface PagoService{
    List<PagoDTO> findAll();
    List<PagoDTO> findByEstado(String estado);
    PagoDTO findById(Long id);
    PagoDTO save(Pago pago);
    PagoDTO convertToDTO(Pago pago);
}
