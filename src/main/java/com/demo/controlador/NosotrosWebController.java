package com.demo.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.demo.model.Usuario;
import com.demo.repositorio.UsuarioRepositorio;

@Controller
public class NosotrosWebController {
	@Autowired
	private UsuarioRepositorio usuariorepositorio;
		
	@GetMapping("/nosotros")
    public String mostrarNosotros(Model model, Authentication au) {
		
		  if(au!=null) {
	        	Usuario usu= usuariorepositorio.findByCorreo(au.getName());
	        	model.addAttribute("nombreCom", usu.getNombreUsuario());
	        }
        return "PaginaWeb/nosotros";
    }
}