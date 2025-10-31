package com.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Generos") 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idGenero")
    private Integer idGenero;

    @Column(name = "nombreGenero", nullable = false, length = 100)
    private String nombreGenero;
}
