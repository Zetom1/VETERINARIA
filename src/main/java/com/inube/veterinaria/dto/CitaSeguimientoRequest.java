package com.inube.veterinaria.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaSeguimientoRequest {
    private Integer idMascota;
    private LocalDateTime fechaCita;
    private String motivo;
    private String observaciones;
}