package com.inube.veterinaria.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "mascota")
@Data
public class MascotaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mascota")
    @SequenceGenerator(name = "seq_mascota", sequenceName = "seq_mascota", allocationSize = 1)
    @Column(name = "id_mascota")
    private Integer idMascota;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "especie")
    private String especie;

    @Column(name = "raza")
    private String raza;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "sexo")
    private String sexo;

    @Column(name = "fecha_ingreso")
    private LocalDateTime fechaIngreso = LocalDateTime.now();

    @Column(name = "fecha_adopcion")
    private LocalDateTime fechaAdopcion;

    @ManyToOne
    @JoinColumn(name = "id_adoptante", referencedColumnName = "id_adoptante")
    private AdoptanteModel adoptante;

    @Column(name = "estado")
    private Integer estado = 1;
}