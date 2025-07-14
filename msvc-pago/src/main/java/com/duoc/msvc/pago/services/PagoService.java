package com.duoc.msvc.pago.services;

import com.duoc.msvc.pago.models.Pago;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import java.util.List;

public interface PagoService{
    CollectionModel<EntityModel<Pago>> findAll();
    CollectionModel<EntityModel<Pago>> findByEstado(String estado);
    EntityModel<Pago> findById(Long id);
    String updateEstadoById(Long id, String nuevoEstado);
    EntityModel<Pago> save(Pago pago);
    EntityModel<Pago> updateById(Long id, Pago pago);
    void deleteById(Long id);
}
