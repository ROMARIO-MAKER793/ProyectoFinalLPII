package com.demo.controlador;

import com.demo.model.Funcion;
import com.demo.model.FuncionDTO;
import com.demo.model.Pelicula;
import com.demo.model.Sala;
import com.demo.service.FuncionService;
import com.demo.service.PeliculaService;
import com.demo.service.SalaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Controller
@RequestMapping("/PaginaAdmin/funciones")
public class FuncionControlador {

    @Autowired
    private FuncionService funcionService;

    @Autowired
    private PeliculaService peliculaService;

    @Autowired
    private SalaService salaService;

    // Formulario 
    @GetMapping("/nuevo/{idPelicula}")
    public String mostrarFormulario(@PathVariable Integer idPelicula, Model model) {
        Pelicula pelicula = peliculaService.buscarPorId(idPelicula);
        model.addAttribute("pelicula", pelicula);
        model.addAttribute("funcion", new Funcion());
        model.addAttribute("salas", salaService.listar()); 

        // Funciones existentes de la película
        List<Funcion> funcionesExistentes = funcionService.listarPorPelicula(idPelicula);
        model.addAttribute("funcionesExistentes", funcionesExistentes);

        return "mantenimientoPeliculas/registrar-funcion";
    }

    
    @PostMapping("/guardar")
    @ResponseBody  // Esto indica que va a devolver JSON
    public Map<String, String> guardarFuncionAjax(@ModelAttribute Funcion funcion,
                                                  @RequestParam("peliculaId") Integer peliculaId) {
        Map<String, String> respuesta = new HashMap<>();
        try {
            Pelicula peli = peliculaService.buscarPorId(peliculaId);
            funcion.setPelicula(peli);

            int duracion = funcion.getPelicula().getDuracion();
            funcion.setHoraFin(funcion.getHoraInicio().plusMinutes(duracion));

            List<Funcion> funcionesSala = funcionService.listarPorSalaYFecha(
                    funcion.getSala().getIdSala(), funcion.getFechaFuncion()
            );

            boolean cruce = funcionesSala.stream().anyMatch(f ->
                    (funcion.getHoraInicio().isBefore(f.getHoraFin()) && funcion.getHoraFin().isAfter(f.getHoraInicio()))
            );

            if (cruce) {
                respuesta.put("status", "cruceHorario");
                return respuesta;
            }

            funcionService.guardar(funcion);
            respuesta.put("status", "ok"); // todo correcto
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.put("status", "error");
        }
        return respuesta;
    }


    // Listar todas las funciones
    @GetMapping("/listar")
    public String listarFunciones(Model model,@AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("listaFunciones", funcionService.listar());
        model.addAttribute("adminNombre", userDetails.getUsername());
        return "mantenimientoPeliculas/listar-funciones";
    }
		//Editar
    @GetMapping("/editar/{idFuncion}")
    public String editarFuncion(@PathVariable Integer idFuncion, Model model) {
        Funcion funcion = funcionService.buscarPorId(idFuncion);

        // Si es null (nueva función), asignamos hoy para que el input muestre algo
        if (funcion.getFechaFuncion() == null) {
            funcion.setFechaFuncion(LocalDate.now());
        }
        
        model.addAttribute("funcion", funcion);
        model.addAttribute("peliculas", peliculaService.listar());
        model.addAttribute("salas", salaService.listar());

        List<Funcion> funcionesExistentes = funcionService.listarPorPelicula(funcion.getPelicula().getIdPelicula());
        model.addAttribute("funcionesExistentes", funcionesExistentes);

        return "mantenimientoPeliculas/editar-funcion";
    }

    @PostMapping("/actualizar")
    public String actualizarFuncion(@ModelAttribute Funcion funcion, RedirectAttributes redirectAttributes) {
        try {
            // Cargar la función existente de la BD
            Funcion funcionExistente = funcionService.buscarPorId(funcion.getIdFuncion());
            if (funcionExistente == null) {
                redirectAttributes.addFlashAttribute("msg", "noExiste");
                return "redirect:/PaginaAdmin/funciones/listar";
            }

            // Actualizar solo los campos que cambian
            funcionExistente.setFechaFuncion(funcion.getFechaFuncion());
            funcionExistente.setHoraInicio(funcion.getHoraInicio());
            funcionExistente.setSala(funcion.getSala());

            // Calcular horaFin según duración de la película
            int duracion = funcionExistente.getPelicula().getDuracion();
            funcionExistente.setHoraFin(funcionExistente.getHoraInicio().plusMinutes(duracion));

            // Validar cruce de horarios
            List<Funcion> funcionesSala = funcionService.listarPorSalaYFecha(
                    funcionExistente.getSala().getIdSala(), funcionExistente.getFechaFuncion()
            );

            boolean cruce = funcionesSala.stream().anyMatch(f ->
                    !f.getIdFuncion().equals(funcionExistente.getIdFuncion()) &&
                            (funcionExistente.getHoraInicio().isBefore(f.getHoraFin()) && funcionExistente.getHoraFin().isAfter(f.getHoraInicio()))
            );

            if (cruce) {
                redirectAttributes.addFlashAttribute("msg", "cruceHorario");
                return "redirect:/PaginaAdmin/funciones/editar/" + funcionExistente.getIdFuncion();
            }

            // Guardar actualización
            funcionService.guardar(funcionExistente);
            redirectAttributes.addFlashAttribute("msg", "editOk");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("msg", "error");
        }

        return "redirect:/PaginaAdmin/funciones/listar";
    }


    // Eliminar función
    @GetMapping("/eliminar/{idFuncion}")
    public String eliminarFuncion(@PathVariable Integer idFuncion, RedirectAttributes redirectAttributes) {
        try {
            funcionService.eliminar(idFuncion);
            redirectAttributes.addFlashAttribute("msg", "delOk");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("msg", "error");
        }
        return "redirect:/PaginaAdmin/funciones/listar";
    }
    //Metodo obtener funciones exitentes por Fecha
    @GetMapping("/existentesPorFecha/{fecha}")
    @ResponseBody
    public List<FuncionDTO> obtenerFuncionesPorFecha(@PathVariable String fecha) {
    	LocalDate fechaLocalDate = LocalDate.parse(fecha);
    	List<Funcion> funciones = funcionService.listarPorFecha(fechaLocalDate); 

    	List<FuncionDTO> funcionesDto = new ArrayList<>();
    	for(Funcion f : funciones) {
    		FuncionDTO funcionDto = new FuncionDTO();
    		funcionDto.setIdFuncion(f.getIdFuncion());
    		funcionDto.setTituloPelicula(f.getPelicula().getTitulo());
    		funcionDto.setNombreSala(f.getSala().getNombreSala());
    		funcionDto.setFechaFuncion(f.getFechaFuncion());
    		funcionDto.setHoraInicio(f.getHoraInicio());
    		funcionDto.setHoraFin(f.getHoraFin());
    		funcionesDto.add(funcionDto);
    	}
        return funcionesDto;
    }
    
}


