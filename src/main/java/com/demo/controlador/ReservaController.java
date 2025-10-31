package com.demo.controlador;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.model.Asiento;
import com.demo.model.Cliente;
import com.demo.model.Funcion;
import com.demo.model.FuncionAsiento;
import com.demo.model.Reserva;
import com.demo.model.ReservaDetalle;
import com.demo.model.Usuario;
import com.demo.repositorio.AsientoRepositorio;
import com.demo.repositorio.ClienteRepositorio;
import com.demo.repositorio.FuncionRepositorio;
import com.demo.repositorio.FuncionesAsientosRepositorio;
import com.demo.repositorio.ReservaDetalleRepositorio;
import com.demo.repositorio.ReservaRepositorio;
import com.demo.repositorio.UsuarioRepositorio;

@Controller
@RequestMapping("/reserva")
public class ReservaController {

    @Autowired
    private FuncionRepositorio funcionRepository;

    @Autowired
    private FuncionesAsientosRepositorio funcionAsientoRepository;

    @Autowired
    private AsientoRepositorio asientoRepository;

    @Autowired
    private ReservaRepositorio reservaRepository;

    @Autowired
    private ReservaDetalleRepositorio reservaDetalleRepository;

    @Autowired
    private UsuarioRepositorio usuariorepositorio;

    @Autowired
    private ClienteRepositorio clienteRepository;

    // -------------------------------
    @GetMapping("/seleccionar/{idFuncion}")
    public String seleccionarAsientos(@PathVariable Integer idFuncion, Model model, Authentication au) {
        Funcion funcion = funcionRepository.findById(idFuncion)
                .orElseThrow(() -> new IllegalArgumentException("Función no encontrada: " + idFuncion));

        List<FuncionAsiento> asientos = funcionAsientoRepository.findByFuncion(funcion);

        if (asientos == null || asientos.isEmpty()) {
            for (int i = 1; i <= 30; i++) {
                Asiento asiento = new Asiento();
                asiento.setSala(funcion.getSala());
                asiento.setCodigoAsiento("A" + i);
                asiento.setEstado("Disponible");
                asiento = asientoRepository.save(asiento);

                FuncionAsiento fa = new FuncionAsiento();
                fa.setFuncion(funcion);
                fa.setAsiento(asiento);
                fa.setEstado("LIBRE");
                funcionAsientoRepository.save(fa);
            }
            asientos = funcionAsientoRepository.findByFuncion(funcion);
        }

        List<FuncionAsiento> zonaSuperior = asientos.subList(0, Math.min(10, asientos.size()));
        List<FuncionAsiento> zonaMedia    = asientos.size() > 10 ? asientos.subList(10, Math.min(20, asientos.size())) : new ArrayList<>();
        List<FuncionAsiento> zonaInferior = asientos.size() > 20 ? asientos.subList(20, Math.min(30, asientos.size())) : new ArrayList<>();

        model.addAttribute("funcion", funcion);
        model.addAttribute("zonaSuperior", zonaSuperior);
        model.addAttribute("zonaMedia", zonaMedia);
        model.addAttribute("zonaInferior", zonaInferior);

        if (au != null) {
            Usuario usu = usuariorepositorio.findByCorreo(au.getName());
            model.addAttribute("nombreCom", usu.getNombreUsuario());
        }

        return "PaginaWeb/seleccionarAsientos";
    }


    // Guardar reserva
    // -------------------------------
    @PostMapping("/guardar")
    public String guardarReserva(
            @RequestParam("idFuncion") Integer idFuncion,
            @RequestParam("asientosSeleccionados") List<Integer> idsFuncionAsiento,
            @RequestParam("metodoPago") String metodoPago,
            Model model,
            Authentication auth) {

        // 1. Obtener usuario autenticado
        if (auth == null) {
            model.addAttribute("error", "Debe iniciar sesión para realizar una reserva.");
            return "PaginaWeb/seleccionarAsientos";
        }

        Usuario usuario = usuariorepositorio.findByCorreo(auth.getName());
        if (usuario == null) {
            model.addAttribute("error", "Usuario no encontrado.");
            return "PaginaWeb/seleccionarAsientos";
        }

        // 2. Obtener cliente asociado
        Cliente cliente = clienteRepository.findByUsuario(usuario);
        if (cliente == null) {
            model.addAttribute("error", "No se encontró cliente asociado al usuario.");
            return "PaginaWeb/seleccionarAsientos";
        }

        // 3. Obtener función y verificar que tenga película
        Funcion funcion = funcionRepository.findById(idFuncion)
                .orElseThrow(() -> new IllegalArgumentException("Función no encontrada: " + idFuncion));

        if (funcion.getPelicula() == null) {
            model.addAttribute("error", "La función no tiene película asociada.");
            return "PaginaWeb/seleccionarAsientos";
        }

        // 4. Validar duplicados en los asientos seleccionados
        Set<Integer> asientosUnicos = new HashSet<>();
        for (Integer idFA : idsFuncionAsiento) {
            if (!asientosUnicos.add(idFA)) {
                model.addAttribute("error", "El asiento seleccionado se repite: " + idFA);
                model.addAttribute("funcion", funcion);
                return "PaginaWeb/seleccionarAsientos";
            }
        }

        // 5. Crear reserva
        String metodoFinal = "tarjeta".equalsIgnoreCase(metodoPago) ? "TARJETA" : "CONTRA_ENTREGA";

        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setFuncion(funcion);
        reserva.setFechaReserva(LocalDateTime.now());
        reserva.setMetodoPago(metodoFinal);
        reserva.setEstado(metodoFinal.equals("TARJETA") ? "PAGADO" : "PENDIENTE");
        reserva.setTotal(BigDecimal.valueOf(idsFuncionAsiento.size() * 20.00));
        reserva = reservaRepository.save(reserva);

        // 6. Guardar detalles y actualizar estado de los FuncionAsiento
        List<ReservaDetalle> detalles = new ArrayList<>();
        for (Integer idFA : idsFuncionAsiento) {
            FuncionAsiento fa = funcionAsientoRepository.findById(idFA)
                    .orElseThrow(() -> new IllegalArgumentException("FuncionAsiento no encontrado: " + idFA));

            if ("RESERVADO".equalsIgnoreCase(fa.getEstado())) {
                model.addAttribute("error", "El asiento " + fa.getAsiento().getCodigoAsiento() + " ya está reservado");
                model.addAttribute("funcion", funcion);
                return "PaginaWeb/seleccionarAsientos";
            }

            // Actualizar estado
            fa.setEstado("RESERVADO");
            funcionAsientoRepository.save(fa);

            // Crear detalle de reserva
            ReservaDetalle detalle = new ReservaDetalle();
            detalle.setReserva(reserva);
            detalle.setAsiento(fa.getAsiento());
            detalle.setNumeroAsiento(fa.getAsiento().getCodigoAsiento() != null 
                                     ? fa.getAsiento().getCodigoAsiento() 
                                     : "No asignado");
            detalle.setPrecioUnitario(BigDecimal.valueOf(20.00));
            detalles.add(detalle);
        }

        reservaDetalleRepository.saveAll(detalles);
        reserva.setDetalles(detalles);

        // 7. Agregar nombre de usuario al modelo
        model.addAttribute("nombreCom", usuario.getNombreUsuario() != null ? usuario.getNombreUsuario() : "Usuario");

        // 8. Mensaje de éxito y reserva
        model.addAttribute("mensaje", "🎟️ ¡Reserva realizada con éxito!");
        model.addAttribute("reserva", reserva);

        return "PaginaWeb/reservaExitosa";
    }


}

