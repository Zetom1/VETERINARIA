package com.inube.veterinaria.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistorialVacunacionRequest {
    private Integer idMascota;
    private Integer idVacuna;
    private LocalDateTime fechaAplicacion;
}