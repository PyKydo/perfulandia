package com.duoc.msvc.pago.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    private LocalDateTime fecha;

    @Column(nullable = false)
    @NotBlank(message = "El campo metodo no puede estar vacio")
    private String metodo; // Efectivo, Débito, Crédito, etc.

    @Column(nullable = false)
    @NotBlank(message = "El campo metodo no puede estar vacio")
    private String estado; // Pendiente, Completado, Rechazado, etc.

    @Column(nullable = false)
    @Positive
    @NotNull(message = "El campo monto no puede estar vacio")
    private BigDecimal monto;



}
