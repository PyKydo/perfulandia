package com.duoc.msvc.pago.services;

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
    public List<Pago> findAll() {
        return this.pagoRepository.findAll();
    }

    @Override
    public List<Pago> findByEstado(String estado) {
        return this.pagoRepository.findByEstado(estado);
    }

    @Override
    public Pago findById(Long id) {
        return this.pagoRepository.findById(id).orElseThrow(
                () -> new PagoException("El pago con id " + id + " no se encuentra en la base de datos")
        );
    }

    @Override
    public Pago save(Pago pago) {
        return this.pagoRepository.save(pago);
    }
}
