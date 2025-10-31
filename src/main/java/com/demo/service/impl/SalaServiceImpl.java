package com.demo.service.impl;

import com.demo.model.Sala;
import com.demo.repositorio.SalaRepositorio;
import com.demo.service.SalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaServiceImpl implements SalaService {

    @Autowired
    private SalaRepositorio salaRepositorio;

    @Override
    public List<Sala> listar() {
        return salaRepositorio.findAll();
    }

    @Override
    public Sala obtenerPorId(Integer id) {
        return salaRepositorio.findById(id).orElse(null);
    }
}
