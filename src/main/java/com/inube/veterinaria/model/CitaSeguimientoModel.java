package com.inube.veterinaria.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "cita_seguimiento")
@Data
public class CitaSeguimientoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cita_seguimiento")
    @SequenceGenerator(name = "seq_cita_seguimiento", sequenceName = "seq_cita_seguimiento", allocationSize = 1)
    @Column(name = "id_cita")
    private Integer idCita;

    @ManyToOne
    @JoinColumn(name = "id_mascota", referencedColumnName = "id_mascota", nullable = false)
    private MascotaModel mascota;

    @Column(name = "fecha_cita")
    private LocalDateTime fechaCita;

    @Column(name = "motivo")
    private String motivo;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "estatus")
    private String estatus;

    @Column(name = "estado")
    private Integer estado = 1;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}