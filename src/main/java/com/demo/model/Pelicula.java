package com.demo.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Peliculas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPelicula") 
    private Integer idPelicula;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "duracion")
    private Integer duracion;

    @Column(name = "idioma")
    private String idioma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idClasificacion", nullable = false)
    private Clasificacion clasificacion;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fechaEstreno")
    private Date fechaEstreno;

    @Column(name = "precio")
    private BigDecimal precio;

    @Column(name = "estado")
    private String estado;

    @Column(name = "imagen")
    private String imagen;
    
    //Agrege esto para elegir solo nuevos
    @Column(name = "is_estreno")
    private boolean isEstreno;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "PeliculaGenero",
        joinColumns = @JoinColumn(name = "idPelicula"),
        inverseJoinColumns = @JoinColumn(name = "idGenero")
    )
    private List<Genero> generos;
}
