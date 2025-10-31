package com.demo.model;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ReservaDetalle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idReserva")
    private Reserva reserva;

  
    @ManyToOne
    @JoinColumn(name = "idAsiento") 
    private Asiento asiento;


    private String numeroAsiento; 
    private BigDecimal precioUnitario;
  
}