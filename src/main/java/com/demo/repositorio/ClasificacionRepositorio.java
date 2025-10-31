package com.demo.repositorio;

import com.demo.model.Clasificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClasificacionRepositorio  extends JpaRepository<Clasificacion, Integer> {

}
