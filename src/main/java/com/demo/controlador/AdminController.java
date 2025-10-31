package com.demo.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

import com.demo.service.ClienteService;
import com.demo.service.PeliculaService;
import com.demo.service.ReservaService;

@Controller
@RequestMapping("/PaginaAdmin")
public class AdminController {

    @Autowired
    private PeliculaService peliculaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/PanelAdmin")
    public String panelAdmin(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            int totalPeliculas = (peliculaService.listar() != null) ? peliculaService.listar().size() : 0;
            int totalClientes = (clienteService.listarActivos() != null) ? clienteService.listarActivos().size() : 0;
            int totalReservas = (reservaService.listar() != null) ? reservaService.listar().size() : 0;

            model.addAttribute("totalPeliculas", totalPeliculas);
            model.addAttribute("totalClientes", totalClientes);
            model.addAttribute("totalReservas", totalReservas);
            model.addAttribute("currentUri", request.getRequestURI());

            model.addAttribute("adminNombre", userDetails.getUsername());

        } catch (Exception e) {
            System.err.println("❌ Error al cargar estadísticas del panel: " + e.getMessage());
            model.addAttribute("totalPeliculas", 0);
            model.addAttribute("totalClientes", 0);
            model.addAttribute("totalReservas", 0);
            model.addAttribute("currentUri", "");
            model.addAttribute("adminNombre", "Admin");
        }

        return "PaginaAdmin/PanelAdmin";
    }
}

