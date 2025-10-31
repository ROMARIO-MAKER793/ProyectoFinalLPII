package com.demo.service.impl;

import com.demo.model.Genero;
import com.demo.repositorio.GeneroRepositorio;
import com.demo.service.GeneroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneroServiceImpl implements GeneroService {

    @Autowired
    private GeneroRepositorio generoRepository;

    @Override
    public List<Genero> listarTodos() {
        return generoRepository.findAll();
    }
}
