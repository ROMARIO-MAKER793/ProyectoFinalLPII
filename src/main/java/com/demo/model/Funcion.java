package com.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Funciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFuncion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idPelicula")
    private Pelicula pelicula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSala")
    private Sala sala;

    private LocalDate fechaFuncion;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}