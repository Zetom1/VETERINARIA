package com.inube.veterinaria.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "chip_rastreo")
@Data
public class ChipRastreoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_chip_rastreo")
    @SequenceGenerator(name = "seq_chip_rastreo", sequenceName = "seq_chip_rastreo", allocationSize = 1)
    @Column(name = "id_chip")
    private Integer idChip;

    @Column(name = "codigo_chip")
    private String codigoChip;

    @Column(name = "fecha_implantacion")
    private LocalDateTime fechaImplantacion;

    @Column(name = "fabricante")
    private String fabricante;

    // Relación 1 a 1: Una mascota solo puede tener un registro de chip aquí
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mascota", referencedColumnName = "id_mascota", nullable = false, unique = true)
    private MascotaModel mascota;

    @Column(name = "estado")
    private Integer estado = 1;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}