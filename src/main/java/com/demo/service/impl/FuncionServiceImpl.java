package com.demo.service.impl;

import com.demo.model.Funcion;
import com.demo.model.Pelicula;
import com.demo.model.Sala;
import com.demo.repositorio.FuncionRepositorio;
import com.demo.repositorio.PeliculaRepositorio;
import com.demo.repositorio.SalaRepositorio;
import com.demo.service.FuncionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuncionServiceImpl implements FuncionService {

    @Autowired
    private FuncionRepositorio funcionRepositorio;
    @Autowired
    private PeliculaRepositorio peliculaRepositorio;
    @Autowired
    private SalaRepositorio salaRepositorio;

    @Override
    public List<Funcion> listar() {
        return funcionRepositorio.findAll();
    }

    @Override
    public Funcion guardar(Funcion funcion) {
        funcion.setPelicula(peliculaRepositorio.findById(funcion.getPelicula().getIdPelicula())
                .orElseThrow(() -> new RuntimeException("Película no encontrada")));
        funcion.setSala(salaRepositorio.findById(funcion.getSala().getIdSala())
                .orElseThrow(() -> new RuntimeException("Sala no encontrada")));
        return funcionRepositorio.save(funcion);
    }

    @Override
    public Funcion buscarPorId(Integer id) {
        return funcionRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Función no encontrada"));
    }

    @Override
    public void eliminar(Integer id) {
        funcionRepositorio.deleteById(id);
    }

    @Override
    public List<Funcion> filtrarPorFechaYGenero(LocalDate fecha, List<Integer> generosSeleccionados) {
        return funcionRepositorio.findAll().stream()
                .filter(f -> fecha == null || f.getFechaFuncion().equals(fecha))
                .filter(f -> generosSeleccionados == null || generosSeleccionados.isEmpty() ||
                        f.getPelicula().getGeneros().stream()
                                .anyMatch(g -> generosSeleccionados.contains(g.getIdGenero())))
                .toList();
    }

    @Override
    public List<Funcion> listarPorSalaYFecha(Integer idSala, LocalDate fecha) {
        return funcionRepositorio.listarPorSalaYFecha(idSala, fecha);
    }

    @Override
    public List<Funcion> listarPorPelicula(Integer idPelicula) {
        return funcionRepositorio.findByPelicula_IdPelicula(idPelicula);
    }

    @Override
    public List<LocalDate> obtenerFechasPorPelicula(Integer idPelicula) {
        return funcionRepositorio.obtenerFechasPorPelicula(idPelicula);
    }

    @Override
    public List<LocalDate> obtenerFechasDisponibles() {
        return funcionRepositorio.findAll().stream()
                .map(Funcion::getFechaFuncion)
                .distinct()
                .sorted()
                .toList();
    }
    @Override
    public List<Funcion> listarPorFecha(LocalDate fecha) {
        return funcionRepositorio.findByFechaFuncion(fecha);
    }
}
