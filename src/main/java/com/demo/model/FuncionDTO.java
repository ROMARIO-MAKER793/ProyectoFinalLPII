package com.demo.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class FuncionDTO {
	private Integer idFuncion;

	
    private String tituloPelicula;


    private String nombreSala;

    private LocalDate fechaFuncion;
    
    private LocalTime horaInicio;
    
    private LocalTime horaFin;
}
