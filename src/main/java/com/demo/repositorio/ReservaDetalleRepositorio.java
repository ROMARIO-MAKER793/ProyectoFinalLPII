package com.demo.repositorio;

import com.demo.model.ReservaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaDetalleRepositorio extends JpaRepository<ReservaDetalle, Integer>{

}
