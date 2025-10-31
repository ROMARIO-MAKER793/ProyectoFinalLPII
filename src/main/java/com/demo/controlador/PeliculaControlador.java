package com.demo.controlador;

import com.demo.model.Pelicula;
import com.demo.model.Clasificacion;
import com.demo.model.Genero;
import com.demo.service.FuncionService;
import com.demo.service.PeliculaService;
import com.demo.repositorio.ClasificacionRepositorio;
import com.demo.repositorio.GeneroRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

@Controller
@RequestMapping("/mantenimientoPeliculas")
public class PeliculaControlador {

    @Autowired
    private PeliculaService peliculaService;

    @Autowired
    private GeneroRepositorio generoRepository;

    @Autowired
    private FuncionService funcionService;

    @Autowired
    private ClasificacionRepositorio clasificacionRepository;

    // Listar películas
    @GetMapping("/lista")
    public String listaPeliculas(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("listaPeliculas", peliculaService.listarActivas());
        model.addAttribute("adminNombre", userDetails.getUsername());
        return "mantenimientoPeliculas/lista-peliculas";
    }

    // Nuevo formulario
    @GetMapping("/nuevo")
    public String nuevaPelicula(Model model) {
        Pelicula pelicula = new Pelicula();
        pelicula.setClasificacion(new Clasificacion());
        pelicula.setGeneros(new ArrayList<>());
        model.addAttribute("pelicula", pelicula);
        model.addAttribute("generos", generoRepository.findAll());
        model.addAttribute("clasificaciones", clasificacionRepository.findAll());
        return "mantenimientoPeliculas/form-pelicula";
    }

    // Guardar o actualizar película
    @PostMapping("/guardar")
    public String guardarPelicula(@ModelAttribute("pelicula") Pelicula pelicula,
                                  @RequestParam("imagenArchivo") MultipartFile imagenArchivo,
                                  RedirectAttributes redirectAttributes) {
        try {
            // Manejo de imagen
        	// Manejo de imagen con redimension y compresión
        	String carpeta = "src/main/resources/static/img/";
        	if (!imagenArchivo.isEmpty()) {
        	    String nombreArchivo = imagenArchivo.getOriginalFilename();
        	    Path ruta = Paths.get(carpeta + nombreArchivo);
        	    //
        	    Files.write(ruta, imagenArchivo.getBytes());
                pelicula.setImagen(nombreArchivo);
            }
            
            // 👇 Nueva lógica: marcar automáticamente como estreno si es nueva
            if (pelicula.getIdPelicula() == null) {
                pelicula.setEstreno(true);
            }

        	   /* // Leer la imagen subida
        	    BufferedImage img = ImageIO.read(imagenArchivo.getInputStream());

        	    // Redimensionar a un máximo de 800x600 px
        	    int anchoMax = 800;
        	    int altoMax = 600;
        	    int ancho = img.getWidth();
        	    int alto = img.getHeight();
        	    double escala = Math.min((double) anchoMax / ancho, (double) altoMax / alto);

        	    if (escala < 1) {
        	        int nuevoAncho = (int) (ancho * escala);
        	        int nuevoAlto = (int) (alto * escala);

        	        BufferedImage imgRedimensionada = new BufferedImage(nuevoAncho, nuevoAlto, BufferedImage.TYPE_INT_RGB);
        	        Graphics2D g = imgRedimensionada.createGraphics();
        	        g.drawImage(img, 0, 0, nuevoAncho, nuevoAlto, null);
        	        g.dispose();

        	        // Guardar imagen comprimida
        	        try (OutputStream os = Files.newOutputStream(ruta)) {
        	            ImageIO.write(imgRedimensionada, "jpg", os);
        	        }
        	    } else {
        	        // Si la imagen es pequeña, guardar tal cual
        	        Files.copy(imagenArchivo.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);
        	    }

        	    pelicula.setImagen(nombreArchivo);*/

            
             else if (pelicula.getIdPelicula() != null) {
                // Si es update y no suben imagen, mantener la actual
                Pelicula peliculaExistente = peliculaService.buscarPorId(pelicula.getIdPelicula());
                pelicula.setImagen(peliculaExistente.getImagen());
            }

            // Manejo de géneros (ManyToMany)
            if (pelicula.getGeneros() != null && !pelicula.getGeneros().isEmpty()) {
                List<Genero> generos = generoRepository.findAllById(
                        pelicula.getGeneros().stream()
                                .map(Genero::getIdGenero)
                                .collect(Collectors.toList())
                );
                pelicula.setGeneros(generos);
            }

            // Manejo de clasificación (ManyToOne)
            if (pelicula.getClasificacion() != null && pelicula.getClasificacion().getIdClasificacion() != null) {
                Clasificacion clas = clasificacionRepository.findById(pelicula.getClasificacion().getIdClasificacion())
                        .orElse(null);
                pelicula.setClasificacion(clas);
            }

            // Guardar o actualizar
            peliculaService.guardar(pelicula);
            redirectAttributes.addFlashAttribute("msg", "ok");

            return "redirect:/mantenimientoPeliculas/nuevo";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("msg", "exception");
            return "redirect:/mantenimientoPeliculas/nuevo";
        }
    }

    // Editar película
    @GetMapping("/editar/{id}")
    public String editarPelicula(@PathVariable("id") Integer id, Model model) {
        Pelicula pelicula = peliculaService.buscarPorId(id);
        model.addAttribute("pelicula", pelicula);
        model.addAttribute("generos", generoRepository.findAll());
        model.addAttribute("clasificaciones", clasificacionRepository.findAll());
        return "mantenimientoPeliculas/form-pelicula";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPelicula(@PathVariable("id") Integer id) {
        Pelicula pelicula = peliculaService.buscarPorId(id);
        if (pelicula != null) {
            pelicula.setEstado("INACTIVO"); 
            peliculaService.guardar(pelicula); 
        }
        return "redirect:/mantenimientoPeliculas/lista";
    }
    // Ver detalles de película
    
    @GetMapping("/ver/{id}")
    public String verPelicula(@PathVariable("id") Integer id, Model model) {
        Pelicula pelicula = peliculaService.buscarPorId(id);

        String generosStr = "No disponible";
        if (pelicula.getGeneros() != null && !pelicula.getGeneros().isEmpty()) {
            generosStr = pelicula.getGeneros()
                    .stream()
                    .map(Genero::getNombreGenero)
                    .collect(Collectors.joining(", "));
        }

        model.addAttribute("pelicula", pelicula);
        model.addAttribute("generosStr", generosStr);
        return "mantenimientoPeliculas/ver-pelicula";
    }
}

