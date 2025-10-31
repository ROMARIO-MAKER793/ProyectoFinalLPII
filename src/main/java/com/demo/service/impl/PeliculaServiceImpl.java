package com.demo.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.model.Pelicula;
import com.demo.repositorio.PeliculaRepositorio;
import com.demo.service.PeliculaService;

@Service
public class PeliculaServiceImpl implements PeliculaService {

    @Autowired
    private PeliculaRepositorio peliculaRepository;
    
    
    @Override
    public List<Pelicula> listar() {
    	 System.out.println("📽️ Listando películas por ID descendente...");
        return peliculaRepository.findAllByOrderByIdPeliculaDesc();
    }
    
	/*
	 * @Override public List<Pelicula> listar() { return
	 * peliculaRepository.findAll(); }
	 */
    @Override
    @Transactional
    public Pelicula guardar(Pelicula pelicula) {
        if (pelicula.getIdPelicula() != null) {
           
            Pelicula existente = peliculaRepository.findById(pelicula.getIdPelicula())
                    .orElseThrow(() -> new RuntimeException("Película no encontrada"));
            // Actualizar campos
            existente.setTitulo(pelicula.getTitulo());
            existente.setDescripcion(pelicula.getDescripcion());
            existente.setDuracion(pelicula.getDuracion());
            existente.setIdioma(pelicula.getIdioma());
            existente.setClasificacion(pelicula.getClasificacion());
            existente.setGeneros(pelicula.getGeneros());
            existente.setFechaEstreno(pelicula.getFechaEstreno());
            existente.setPrecio(pelicula.getPrecio());
            existente.setEstado(pelicula.getEstado());
            if (pelicula.getImagen() != null) {
                existente.setImagen(pelicula.getImagen());
            }
            return peliculaRepository.save(existente);
        } else {
            // Es un insert
            return peliculaRepository.save(pelicula);
        }
    }

    @Override
    public Pelicula buscarPorId(Integer id) {
        return peliculaRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        peliculaRepository.deleteById(id);
    }
    
    public List<Pelicula> buscarPorGeneros(List<Integer> idsGeneros) {
        return peliculaRepository.buscarPorGeneros(idsGeneros);
    }
    public List<Pelicula> listarActivas() {
        return peliculaRepository.findByEstado("ACTIVO");
    }
    
    //metodo para buscar y mostrar las pelis de estreno
   	@Override
   	public List<Pelicula> findByIsEstrenoTrue() {
   		
   		return peliculaRepository.findByIsEstrenoTrue();
   	}
}
