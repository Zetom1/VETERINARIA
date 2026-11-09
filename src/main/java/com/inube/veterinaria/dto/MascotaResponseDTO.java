package com.inube.veterinaria.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MascotaResponseDTO {
    private Integer idMascota;
    private String nombre;
    private String especie;
    private String raza;
    private Integer edad;
    private String sexo;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaAdopcion;
    private Integer idAdoptante;
    private String nombreAdoptante;
    private Integer estado;
}