package com.demo.controlador;

import com.demo.model.Pelicula;
import com.demo.model.Usuario;
import com.demo.repositorio.PeliculaRepositorio;
import com.demo.repositorio.UsuarioRepositorio;
import com.demo.service.PeliculaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.Authenticator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PaginaWebController {
	@Autowired
	private UsuarioRepositorio usuariorepositorio;
    @Autowired
    private PeliculaService peliculaRepositorio;

   /* @GetMapping({"/","PaginaWeb/index"})
    public String mostrarIndex(Model model, Authentication au) {
        List<Pelicula> peliculas = peliculaRepositorio.listarActivas();*/
    @GetMapping({"/","/PaginaWeb/index"})
	 public String mostrarInicio(Model model, Authentication au) {
	     List<Pelicula> estrenos = peliculaRepositorio.findByIsEstrenoTrue()
	                                                  .stream()
	                                                  .sorted((a, b) -> b.getIdPelicula().compareTo(a.getIdPelicula()))
	                                                  .toList();

        List<Pelicula> novedades =  peliculaRepositorio.listarActivas() .stream()
                .sorted((p1, p2) -> p2.getIdPelicula().compareTo(p1.getIdPelicula()))
                .limit(15)
                .collect(Collectors.toList());
        
        if(au!=null) {
        	Usuario usu= usuariorepositorio.findByCorreo(au.getName());
        	model.addAttribute("nombreCom", usu.getNombreUsuario());
        }
        
        model.addAttribute("peliculas", estrenos);
        model.addAttribute("novedades", novedades);
        return "PaginaWeb/index";
    }
    //Para mostrar en el carrusel solo las peliculas de estreno por orden ascedente
 	 /*@GetMapping({"/","/PaginaWeb/index"})
 	 public String mostrarInicio(Model model, Authentication au) {
 	     List<Pelicula> estrenos = peliculaRepositorio.findByIsEstrenoTrue()
 	                                                  .stream()
 	                                                  .sorted((a, b) -> b.getIdPelicula().compareTo(a.getIdPelicula()))
 	                                                  .toList();
 	     
 	    List<Pelicula> novedades = peliculas.stream()
                .sorted((p1, p2) -> p2.getIdPelicula().compareTo(p1.getIdPelicula()))
                .limit(15)
                .collect(Collectors.toList());
 	    
 	    if(au!=null) {
        	Usuario usu= usuariorepositorio.findByCorreo(au.getName());
        	model.addAttribute("nombreCom", usu.getNombreUsuario());
        }
 	   System.out.println("🎞️ Estrenos encontrados: " + estrenos.size());
 	    estrenos.forEach(p -> System.out.println("➡ " + p.getTitulo()));
 	     model.addAttribute("peliculas", estrenos);
 	     model.addAttribute("novedades", novedades);
 	     return "PaginaWeb/index";
 	 }*/
    
    /////
    /*@GetMapping({"/", "/PaginaWeb/index"})
    public String mostrarInicio(Model model, Authentication au) {
        // 🔹 1. Traer todas las películas
        List<Pelicula> todas = peliculaRepositorio.findByIsEstrenoTrue();

        // 🔹 2. Filtrar estrenos
        List<Pelicula> estrenos = todas.stream()
                .filter(Pelicula::isEstreno)
                .sorted((a, b) -> b.getIdPelicula().compareTo(a.getIdPelicula()))
                .collect(Collectors.toList());

        // 🔹 3. Tomar las demás como novedades (o simplemente las más recientes)
        List<Pelicula> novedades = todas.stream()
                .filter(p -> !p.isEstreno()) // solo las que no son estrenos
                .sorted((p1, p2) -> p2.getIdPelicula().compareTo(p1.getIdPelicula()))
                .limit(15)
                .collect(Collectors.toList());

        // 🔹 4. Mostrar usuario logueado (si lo hay)
        if (au != null) {
            Usuario usu = usuariorepositorio.findByCorreo(au.getName());
            model.addAttribute("nombreCom", usu.getNombreUsuario());
        }

        // 🔹 5. Logs de control
        System.out.println("🎞️ Estrenos encontrados: " + estrenos.size());
        estrenos.forEach(p -> System.out.println("➡ " + p.getTitulo()));

        // 🔹 6. Enviar datos al modelo
        model.addAttribute("peliculas", estrenos);  // para el carrusel
        model.addAttribute("novedades", novedades); // para la sección de novedades

        return "PaginaWeb/index";
    }*/

}

