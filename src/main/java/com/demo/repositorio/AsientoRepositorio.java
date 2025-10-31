package com.demo.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.model.Asiento;
import com.demo.model.Sala;

@Repository
public interface AsientoRepositorio extends JpaRepository<Asiento, Integer> {
	 List<Asiento> findBySala(Sala sala);
}
