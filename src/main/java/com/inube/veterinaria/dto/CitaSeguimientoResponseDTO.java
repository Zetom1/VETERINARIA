package com.inube.veterinaria.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaSeguimientoResponseDTO {
    private Integer idCita;
    private Integer idMascota;
    private String nombreMascota;
    private LocalDateTime fechaCita;
    private String motivo;
    private String observaciones;
    private String estatus;
    private Integer estado;
    private LocalDateTime fechaRegistro;
}