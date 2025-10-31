package com.demo.controlador;

import com.demo.model.Usuario;
import com.demo.repositorio.UsuarioRepositorio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.*;

@Controller
public class LoginControlador {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String mostrarLogin(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               Model model) {
        if (error != null) model.addAttribute("mensajeError", "❌ Credenciales incorrectas.");
        if (logout != null) model.addAttribute("mensajeLogout", "Sesión cerrada correctamente.");
        return "Login/login";
    }

    @PostMapping("/procesarLogin")
    public String procesarLogin(@RequestParam("correo") String correo,
                                @RequestParam("clave") String clave,
                                HttpSession session) {

        Usuario usuario = usuarioRepositorio.findByCorreo(correo);

        if (usuario != null && passwordEncoder.matches(clave, usuario.getClave())) {
            session.setAttribute("usuarioLogueado", usuario);

            if ("ADM".equals(usuario.getRol())) {
                return "redirect:/PaginaAdmin/PanelAdmin";
            } else {
                return "redirect:/PaginaWeb/index";
            }
        }

        return "redirect:/login?error=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}

