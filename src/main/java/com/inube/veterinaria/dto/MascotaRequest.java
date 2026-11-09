package com.inube.veterinaria.dto;

import lombok.Data;

@Data
public class MascotaRequest {
    private String nombre;
    private String especie;
    private String raza;
    private Integer edad;
    private String sexo;
    private Integer idAdoptante; // opcional: solo si la mascota ya se registra adoptada
}