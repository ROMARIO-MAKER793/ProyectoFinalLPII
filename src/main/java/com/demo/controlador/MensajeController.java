package com.demo.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class MensajeController {
	
	 @PostMapping("/enviarMensaje")
	    public String enviarMensaje(
	            @RequestParam("nombre") String nombre,
	            @RequestParam("email") String email,
	            @RequestParam("mensaje") String mensaje,
	            Model model) {

	        //  Simulamos envío (solo mostramos en consola)
	        System.out.println("📩 Nuevo mensaje recibido:");
	        System.out.println("Nombre: " + nombre);
	        System.out.println("Correo: " + email);
	        System.out.println("Mensaje: " + mensaje);

	        //  Enviamos variable para mostrar modal en la vista
	        model.addAttribute("mensajeEnviado", true);
	        model.addAttribute("nombreRemitente", nombre);

	        //  Retornamos la misma vista contacto.html
	        return "PaginaWeb/contacto";
	    }
}