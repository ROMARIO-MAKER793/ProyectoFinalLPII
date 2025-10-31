package com.demo.service;

import com.demo.model.Funcion;
import java.time.LocalDate;
import java.util.List;

public interface FuncionService {
    List<Funcion> listar();
    Funcion guardar(Funcion funcion);
    Funcion buscarPorId(Integer id);
    void eliminar(Integer id);

    // Filtrados y relaciones
    List<Funcion> listarPorSalaYFecha(Integer idSala, LocalDate fecha);
    List<Funcion> listarPorPelicula(Integer idPelicula);
    List<Funcion> filtrarPorFechaYGenero(LocalDate fecha, List<Integer> idsGeneros);

   
    List<LocalDate> obtenerFechasPorPelicula(Integer idPelicula);
    List<LocalDate> obtenerFechasDisponibles();
	List<Funcion> listarPorFecha(LocalDate fechaLocalDate);
}
