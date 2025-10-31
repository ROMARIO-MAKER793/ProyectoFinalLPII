package com.demo.repositorio;

import com.demo.model.Funcion;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionRepositorio extends JpaRepository<Funcion, Integer> {

    @Query("SELECT f FROM Funcion f WHERE f.sala.idSala = :idSala AND f.fechaFuncion = :fecha")
    List<Funcion> listarPorSalaYFecha(@Param("idSala") Integer idSala, @Param("fecha") LocalDate fecha);

    List<Funcion> findByPelicula_IdPelicula(Integer idPelicula);

    @Query(""" 
           SELECT DISTINCT f
           FROM Funcion f
           JOIN f.pelicula p
           JOIN p.generos g
           WHERE (:fecha IS NULL OR f.fechaFuncion = :fecha)
           AND (:idsGeneros IS NULL OR g.idGenero IN :idsGeneros)
        """)
    List<Funcion> filtrarPorFechaYGenero(@Param("fecha") LocalDate fecha,
                                         @Param("idsGeneros") List<Integer> idsGeneros);

    @Query("SELECT DISTINCT f.fechaFuncion FROM Funcion f WHERE f.pelicula.idPelicula = :idPelicula ORDER BY f.fechaFuncion ASC")
    List<LocalDate> obtenerFechasPorPelicula(@Param("idPelicula") Integer idPelicula);

    @Query("SELECT DISTINCT f.fechaFuncion FROM Funcion f ORDER BY f.fechaFuncion ASC")
    List<LocalDate> obtenerFechasDisponibles();

	List<Funcion> findByFechaFuncion(LocalDate fecha);
}

    

