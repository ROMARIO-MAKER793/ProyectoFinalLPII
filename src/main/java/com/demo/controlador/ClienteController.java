package com.demo.controlador;

import com.demo.model.Cliente;
import com.demo.model.Usuario;
import com.demo.service.ClienteService;
import com.demo.service.UserDetailsServiceImpl; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/Cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    
    @GetMapping("/listar")
    public String listarClientes(Model model,@AuthenticationPrincipal UserDetails userDetails) {
        List<Cliente> listaClientes = clienteService.listarActivos();
        model.addAttribute("listaClientes", listaClientes);
        model.addAttribute("adminNombre", userDetails.getUsername());
        return "PaginaAdmin/listarClientes"; 
    }

    // ACTUALIZAR cliente desde modal
    @PostMapping("/actualizar")
    public String actualizarCliente(@ModelAttribute Cliente cliente) {
        boolean exito = clienteService.actualizarCliente(cliente);
        if (exito) {
            return "redirect:/Cliente/listar?msg=ok";
        } else {
            return "redirect:/Cliente/listar?msg=fail";
        }
    }

    // “ELIMINAR” cliente 
    @PostMapping("/eliminar")
    public String eliminarCliente(@RequestParam("idCliente") Integer idCliente) {
        boolean exito = clienteService.eliminarCliente(idCliente);
        if (exito) {
            return "redirect:/Cliente/listar?msg=okEliminar";
        } else {
            return "redirect:/Cliente/listar?msg=failEliminar";
        }
    }
}
