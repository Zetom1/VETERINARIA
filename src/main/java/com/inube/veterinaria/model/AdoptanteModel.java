package com.inube.veterinaria.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "adoptante")
@Data
public class AdoptanteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_adoptante")
    @SequenceGenerator(name = "seq_adoptante", sequenceName= "seq_adoptante", allocationSize = 1)
    @Column(name = "id_adoptante")
    private Integer idAdoptante;

    @Column(name = "nombre")
    private String nombre;

    @Column(name="apellido")
    private String apellido;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "correo")
    private String correo;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "estado")
    private Integer estado = 1;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}
