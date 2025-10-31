package com.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FuncionAsiento")
public class FuncionAsiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFuncionAsiento;

    @ManyToOne
    @JoinColumn(name = "idFuncion")
    private Funcion funcion;

    @ManyToOne
    @JoinColumn(name = "idAsiento")
    private Asiento asiento;

    @Column(nullable = false)
    private String estado = "LIBRE";
}
