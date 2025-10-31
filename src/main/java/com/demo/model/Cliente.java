package com.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    private String nombres;
    private String apellidos;
    private String genero; 
    private java.time.LocalDate fechaNacimiento;
    private String telefono;
    @Column(length = 10, nullable = false)
    private String estado = "Activo"; 
}