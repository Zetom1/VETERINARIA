package com.inube.veterinaria.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChipRastreoRequest {
    private String codigoChip;
    private LocalDateTime fechaImplantacion;
    private String fabricante;
    private Integer idMascota;
}