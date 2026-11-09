package com.inube.veterinaria.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChipRastreoResponseDTO {
    private Integer idChip;
    private String codigoChip;
    private LocalDateTime fechaImplantacion;
    private String fabricante;
    private Integer idMascota;
    private String nombreMascota;
    private Integer estado;
    private LocalDateTime fechaRegistro;
}