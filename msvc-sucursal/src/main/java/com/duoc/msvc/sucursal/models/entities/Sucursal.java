package com.duoc.msvc.sucursal.models.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sucursal")
@ToString @Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sucursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sucursal")
    private Long idSucursal;

    @Column(nullable = false)
    @NotBlank(message = "El campo direccion no puede estar vacio")
    private String direccion;

    @Column(nullable = false)
    @NotBlank(message = "El campo región no puede estar vacio")
    private String region;

    @Column(nullable = false)
    @NotBlank(message = "El campo comuna no puede estar vacio")
    private String comuna;

    @Column(nullable = false)
    @NotBlank(message = "El campo cantidad personal no puede estar vacio")
    private String cantidadPersonal;

    @Column(nullable = false)
    @NotBlank(message = "El campo horarios Atencion no puede estar vacio")
    private String horariosAtencion;

    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inventario> inventarios =  new ArrayList<>();
}
