package com.demo.repositorio;

import com.demo.model.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 

@Repository
public interface GeneroRepositorio extends JpaRepository<Genero, Integer> {

}
