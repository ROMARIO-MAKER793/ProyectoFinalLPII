package com.demo.repositorio;

import com.demo.model.Pelicula;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PeliculaRepositorio extends JpaRepository<Pelicula, Integer> {

	@Query("SELECT p FROM Pelicula p JOIN FETCH p.generos g JOIN FETCH p.clasificacion c WHERE p.idPelicula = :id")
	Pelicula findByIdWithGeneroClasificacion(@Param("id") int id);
	
	@Query("SELECT DISTINCT p FROM Pelicula p JOIN p.generos g WHERE g.idGenero IN :idsGeneros")
	List<Pelicula> buscarPorGeneros(@Param("idsGeneros") List<Integer> idsGeneros);
	 List<Pelicula> findByEstado(String estado);
	 List<Pelicula> findByIsEstrenoTrue();
		List<Pelicula> findAllByOrderByIdPeliculaDesc();
}