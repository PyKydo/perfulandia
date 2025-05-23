package com.duoc.msvc.pago.services;

import com.duoc.msvc.pago.models.Pago;

import java.util.List;

public interface PagoService{
    List<Pago> findAll();
    Pago findById(Long id);
    Pago save(Pago pago);
}
