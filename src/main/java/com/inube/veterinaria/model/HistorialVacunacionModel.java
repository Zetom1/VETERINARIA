package com.inube.veterinaria.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_vacunacion")
@Data
public class HistorialVacunacionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_historial_vacunacion")
    @SequenceGenerator(name = "seq_historial_vacunacion", sequenceName = "seq_historial_vacunacion", allocationSize = 1)
    @Column(name = "id_historial")
    private Integer idHistorial;

    @ManyToOne
    @JoinColumn(name = "id_mascota")
    private MascotaModel mascota;

    @ManyToOne
    @JoinColumn(name = "id_vacuna")
    private VacunaModel vacuna;

    @Column(name = "fecha_aplicacion")
    private LocalDateTime fechaAplicacion;

    @Column(name = "estado")
    private Integer estado = 1;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}