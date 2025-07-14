package com.duoc.msvc.sucursal.models.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sucursales")
@ToString @Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    @NotNull(message = "El campo cantidad personal no puede estar vacio")
    private Integer cantidadPersonal;

    @Column(nullable = false)
    @NotBlank(message = "El campo horarios Atencion no puede estar vacio")
    private String horariosAtencion;

    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonManagedReference
    private List<Inventario> inventarios =  new ArrayList<>();
}
