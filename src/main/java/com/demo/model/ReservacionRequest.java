package com.demo.model;

import java.util.List;

import lombok.Data;

@Data
public class ReservacionRequest {
    private Integer funcionId;                
    private List<Integer> funcionAsientoIds; 
    private String metodoPago;                
    private Integer clienteId;               
}
