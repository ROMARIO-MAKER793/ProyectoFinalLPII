	package com.demo.repositorio;
	
	import com.demo.model.Usuario;
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.stereotype.Repository;
	
	@Repository
	public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
	    Usuario findByCorreo(String correo);
	    
	}	
