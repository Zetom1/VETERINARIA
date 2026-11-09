package com.inube.veterinaria.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "vacuna")
@Data
public class VacunaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_vacuna")
    @SequenceGenerator(name = "seq_vacuna", sequenceName = "seq_vacuna", allocationSize = 1)
    @Column(name = "id_vacuna")
    private Integer idVacuna;

    @Column(name = "nombre_vacuna")
    private String nombreVacuna;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estado")
    private Integer estado = 1;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}