package com.demo.controlador;

import com.demo.model.Funcion;
import com.demo.model.Genero;
import com.demo.model.Pelicula;
import com.demo.model.Usuario;
import com.demo.repositorio.UsuarioRepositorio;
import com.demo.service.FuncionService;
import com.demo.service.GeneroService;
import com.demo.service.PeliculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cartelera")
public class CarteleraController {

    @Autowired
    private FuncionService funcionService;

    @Autowired
    private GeneroService generoService;

    @Autowired
    private PeliculaService peliculaService;
    @Autowired
    private UsuarioRepositorio usuariorepositorio;
    
    @GetMapping
    public String mostrarCartelera(Model model, Authentication au) {
        return cargarCartelera(null, null, model,au);
    }

    @GetMapping("/filtrar")
    public String filtrarCartelera(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) List<Integer> generosSeleccionados,
            Model model, Authentication au) {
        return cargarCartelera(fecha, generosSeleccionados, model,au);
    }

        private String cargarCartelera(LocalDate fecha, List<Integer> generosSeleccionados, Model model, Authentication au) {
            List<Funcion> funcionesFiltradas = (fecha == null && (generosSeleccionados == null || generosSeleccionados.isEmpty()))
                    ? funcionService.listar()
                    : funcionService.filtrarPorFechaYGenero(fecha, generosSeleccionados);

            Map<Integer, List<Funcion>> funcionMap = new HashMap<>();
            List<Pelicula> peliculas = peliculaService.listar();

            for (Pelicula pelicula : peliculas) {
                List<Funcion> funcs = funcionesFiltradas.stream()
                        .filter(f -> f.getPelicula().getIdPelicula().equals(pelicula.getIdPelicula()))
                        .toList();
                if (!funcs.isEmpty()) {
                    funcionMap.put(pelicula.getIdPelicula(), funcs);
                }
            }

            peliculas = peliculas.stream()
                    .filter(p -> funcionMap.containsKey(p.getIdPelicula()))
                    .sorted(Comparator.comparing(Pelicula::getIdPelicula).reversed())
                    .toList();


            // Verificar si el usuario al modelo si está autenticado
            if (au != null) {
                Usuario usu = usuariorepositorio.findByCorreo(au.getName());
                if (usu != null) {
                    model.addAttribute("nombreCom", usu.getNombreUsuario());
                }
            }

            model.addAttribute("peliculas", peliculas);
            model.addAttribute("funcionMap", funcionMap);
            model.addAttribute("fechasDisponibles", funcionService.obtenerFechasDisponibles());
            model.addAttribute("generos", generoService.listarTodos());
            model.addAttribute("fechaSeleccionada", fecha);
            model.addAttribute("generosSeleccionados", generosSeleccionados);

            return "PaginaWeb/cartelera";
        }

}
