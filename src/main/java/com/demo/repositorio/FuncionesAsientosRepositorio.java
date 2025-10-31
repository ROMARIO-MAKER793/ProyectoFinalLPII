package com.demo.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.model.Asiento;
import com.demo.model.Funcion;
import com.demo.model.FuncionAsiento;

import jakarta.persistence.LockModeType;

@Repository
public interface FuncionesAsientosRepositorio extends JpaRepository<FuncionAsiento, Integer> {

    Optional<FuncionAsiento> findByFuncion_IdFuncionAndAsiento_IdAsiento(Integer idFuncion, Integer idAsiento);

    List<FuncionAsiento> findByFuncion(Funcion funcion);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT f FROM FuncionAsiento f WHERE f.idFuncionAsiento = :id")
    Optional<FuncionAsiento> findByIdForUpdate(@Param("id") Integer id);
}
