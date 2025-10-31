package com.demo.service;

import com.demo.model.Reserva;
import com.demo.repositorio.ReservaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepositorio reservaRepository;

    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    public long contarReservas() {
        return reservaRepository.count();
    }
}
