package com.duoc.msvc.pago.dtos;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Relation(collectionRelation = "pagos", itemRelation = "pago")
public class PagoHateoasDTO extends RepresentationModel<PagoHateoasDTO> {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "10")
    private Long idPedido;
    @Schema(example = "15000.00")
    private BigDecimal monto;
    @Schema(example = "TARJETA")
    private String metodoPago;
    @Schema(example = "PAGADO")
    private String estado;
    @Schema(example = "2024-06-25T12:34:56")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime fechaPago;
} 