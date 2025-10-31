package com.demo.service;

import com.demo.model.Sala;
import java.util.List;

public interface SalaService {
    List<Sala> listar();
    Sala obtenerPorId(Integer id);
}
