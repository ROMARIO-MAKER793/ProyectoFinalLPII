package com.demo.repositorio;

import com.demo.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaRepositorio extends JpaRepository<Sala, Integer> {

}


