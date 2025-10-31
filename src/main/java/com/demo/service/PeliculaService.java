package com.demo.service;

import java.util.List;
import com.demo.model.Pelicula;

public interface PeliculaService {
    List<Pelicula> listar();
    Pelicula guardar(Pelicula pelicula);
    Pelicula buscarPorId(Integer id);
    void eliminar(Integer id);
    List<Pelicula> findByIsEstrenoTrue();
    List<Pelicula> buscarPorGeneros(List<Integer> idsGeneros);
    public List<Pelicula> listarActivas();
}
