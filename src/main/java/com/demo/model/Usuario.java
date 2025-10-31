package com.demo.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "Usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    private String nombreUsuario;
    private String correo;
    private String clave;
    private String rol;
    
   

}
