package com.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Clasificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clasificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idClasificacion")
    private Integer idClasificacion;

    private String etiqueta;
    private String descripcion;
}
