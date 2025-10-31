package com.demo.repositorio;

import com.demo.model.Cliente;
import com.demo.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Integer> {
	 Cliente findByUsuario(Usuario usuario);

}