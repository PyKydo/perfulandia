package com.duoc.msvc.pago.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @Column(nullable = false)
    @NotBlank(message = "El campo metodo no puede estar vacio")
    private String metodo; // Efectivo, débito, crédito, etc.

    @Column(nullable = false)
    @Positive
    @NotNull(message = "El campo monto no puede estar vacio")
    private Integer monto;

}
