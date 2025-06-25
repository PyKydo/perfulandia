package com.duoc.msvc.pago.services;

import com.duoc.msvc.pago.dtos.PagoHateoasDTO;
import com.duoc.msvc.pago.models.Pago;
import org.springframework.hateoas.CollectionModel;

import java.util.List;

public interface PagoService{
    CollectionModel<PagoHateoasDTO> findAll();
    CollectionModel<PagoHateoasDTO> findByEstado(String estado);
    PagoHateoasDTO findById(Long id);
    String updateEstadoById(Long id, String nuevoEstado);
    PagoHateoasDTO save(Pago pago);
    PagoHateoasDTO updateById(Long id, Pago pago);
    void deleteById(Long id);
}
