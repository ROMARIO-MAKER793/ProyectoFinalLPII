package com.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Salas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSala;

    private String nombreSala;
    private Integer capacidad;
    private String estado; 
}