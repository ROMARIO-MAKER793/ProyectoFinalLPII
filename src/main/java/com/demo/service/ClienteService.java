package com.demo.service;

import com.demo.model.Cliente;
import com.demo.model.Usuario;
import com.demo.repositorio.ClienteRepositorio;
import com.demo.repositorio.UsuarioRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepositorio clienteRepository;
    
    @Autowired
    private UsuarioRepositorio usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Cliente> listarActivos() {
        return clienteRepository.findAll()
                .stream()
                .filter(c -> "Activo".equalsIgnoreCase(c.getEstado()))
                .toList(); 
    }

    public long contarClientes() {
        return clienteRepository.count();
    }
    
    public void registrarUsuarioCliente(Usuario usu, Cliente cli) {
        // Validaciones de Usuario
        if (usu.getCorreo() == null || usu.getCorreo().isBlank()) {
            throw new IllegalArgumentException("El correo no puede estar vacío");
        }
        if (!usu.getCorreo().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("El correo no tiene un formato válido");
        }
        if (usu.getClave() == null || usu.getClave().isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        if (usu.getClave().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }

        // Validaciones de Cliente
        if (cli.getNombres() == null || cli.getNombres().isBlank()) {
            throw new IllegalArgumentException("Los nombres no pueden estar vacíos");
        }
        if (cli.getApellidos() == null || cli.getApellidos().isBlank()) {
            throw new IllegalArgumentException("Los apellidos no pueden estar vacíos");
        }
        if (cli.getGenero() == null || cli.getGenero().isBlank()) {
            throw new IllegalArgumentException("El género es obligatorio");
        }
        if (cli.getFechaNacimiento() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria");
        }
        if (cli.getTelefono() == null || cli.getTelefono().isBlank() || !cli.getTelefono().matches("^\\d{7,15}$")) {
            throw new IllegalArgumentException("El teléfono es obligatorio y debe tener solo números");
        }

        // Preparar datos antes de guardar
        usu.setNombreUsuario(cli.getNombres() + " " + cli.getApellidos());
        usu.setClave(passwordEncoder.encode(usu.getClave()));
        cli.setEstado("Activo");
        usu.setRol("CLIENTE");

        // Guardar en la base de datos
        Usuario nuevoUsu = usuarioRepository.save(usu);
        cli.setUsuario(nuevoUsu);
        clienteRepository.save(cli);
    }
    public boolean actualizarCliente(Cliente clienteActualizado) {
        return clienteRepository.findById(clienteActualizado.getIdCliente())
                .map(cli -> {
                    cli.setNombres(clienteActualizado.getNombres());
                    cli.setApellidos(clienteActualizado.getApellidos());
                    cli.setGenero(clienteActualizado.getGenero());
                    cli.setTelefono(clienteActualizado.getTelefono());
                    cli.setFechaNacimiento(clienteActualizado.getFechaNacimiento());
                  
                    clienteRepository.save(cli);
                    return true;
                })
                .orElse(false);
    }
    public boolean eliminarCliente(Integer idCliente) {
        return clienteRepository.findById(idCliente)
                .map(cli -> {
                    cli.setEstado("Inactivo");
                    clienteRepository.save(cli);
                    return true;
                })
                .orElse(false);
    }
    
    
}