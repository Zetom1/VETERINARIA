package com.inube.veterinaria.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistorialVacunacionResponseDTO {
    private Integer idHistorial;
    private Integer idMascota;
    private String nombreMascota;
    private Integer idVacuna;
    private String nombreVacuna;
    private LocalDateTime fechaAplicacion;
    private Integer estado;
    private LocalDateTime fechaRegistro;
}