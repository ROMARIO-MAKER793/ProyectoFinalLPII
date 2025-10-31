package com.demo.controlador;

import java.util.List;
import java.util.stream.Collectors;

import com.demo.model.Pelicula;
import com.demo.model.Usuario;
import com.demo.repositorio.UsuarioRepositorio;
import com.demo.model.Funcion;
import com.demo.service.FuncionService;
import com.demo.service.PeliculaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PeliculaWebController {
	
	@Autowired
	private UsuarioRepositorio usuariorepositorio;
    @Autowired
    private PeliculaService peliculaService;

    @Autowired
    private FuncionService funcionService;

    @GetMapping("/pelicula/detalle/{id}" )
    public String detallePelicula(@PathVariable Integer id, Model model,Authentication au) {
        // Obtener película por ID
        Pelicula pelicula = peliculaService.buscarPorId(id);
        if (pelicula == null) {
            return "Paginaweb/error"; 
        }

        // Convertir géneros a String
        String generosStr = "No disponible";
        if (pelicula.getGeneros() != null && !pelicula.getGeneros().isEmpty()) {
            generosStr = pelicula.getGeneros()
                                 .stream()
                                 .map(g -> g.getNombreGenero())
                                 .collect(Collectors.joining(", "));
        }

        // Obtener funciones de la película
        List<Funcion> funciones = funcionService.listarPorPelicula(id);
        
        if (au != null) {
            Usuario usu = usuariorepositorio.findByCorreo(au.getName());
            model.addAttribute("nombreCom", usu.getNombreUsuario());
        }
      
        model.addAttribute("pelicula", pelicula);
        model.addAttribute("generosStr", generosStr);
        model.addAttribute("funciones", funciones);

        // Template Thymeleaf público
        return "Paginaweb/detallePeli";
    }
}

